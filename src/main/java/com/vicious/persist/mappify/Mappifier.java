package com.vicious.persist.mappify;

import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.except.InvalidValueException;
import com.vicious.persist.except.NoValuePresentException;
import com.vicious.persist.io.writer.wrapped.WrappedObjectList;
import com.vicious.persist.io.writer.wrapped.WrappedObjectMap;
import com.vicious.persist.io.writer.wrapped.WrappedObject;
import com.vicious.persist.mappify.reflect.ClassData;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.mappify.reflect.TypeInfo;
import com.vicious.persist.mappify.registry.Initializers;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.util.Boxing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A generic Mappifier utility that is able to covert objects to and from maps using Reflection.
 * @author Jack Andersen
 * @since 1.0
 */
public class Mappifier {
    public static final Mappifier DEFAULT = new Mappifier();

    public static Mappifier create() {
        return new Mappifier();
    }

    private final Initializers.DynamicConverter<?> converter = (field, index, object)-> this.unmappifyValue(field,null,field.objectified(),object,0);
    private boolean applyCommentsOnReservedFields = false;
    private boolean forceC_NAME = false;

    private Mappifier() {
    }

    /**
     * Builder method that configures the mappifier to mark reserved fields with warning comments.
     * Defaults to false.
     *
     * @param setting the setting.
     * @return self
     */
    public Mappifier applyCommentsOnReservedFields(boolean setting) {
        this.applyCommentsOnReservedFields = setting;
        return this;
    }

    /**
     * Builder method that configures the mappifier to require that all class names saved to have a {@link com.vicious.persist.annotations.C_NAME} present and registered beforehand.
     * If a C_NAME annotation is not present the class will be saved using the java canonical name.
     * Defaults to false.
     *
     * @param setting the setting.
     * @return self
     */
    public Mappifier forceC_NAME(boolean setting) {
        this.forceC_NAME = setting;
        return this;
    }

    /**
     * Converts an object into a WrappedObjectMap using its relevant Fields marked with {@link com.vicious.persist.annotations.Save}
     *
     * @param object the object to mappify.
     * @return the object's map representation.
     */
    public WrappedObjectMap mappify(Object object) {
        return mappify(Context.of(object));
    }

    private WrappedObjectMap mappify(Context context) {
        WrappedObjectMap output = new WrappedObjectMap();
        context.forEach(fieldData -> {
            output.put(fieldData.getName(), mappify(fieldData, context, fieldData.objectified()));
        });
        if (context.hasTransformations()) {
            output.put(Reserved.TRANSFORMER_VER, WrappedObject.of(context.getTransformerVer(), reservedComment()));
        }
        return output;
    }

    private WrappedObject mappify(FieldData<?> data, Context context, boolean objectifyStatics) {
        try {
            return mappifyValue(data, data.get(context), objectifyStatics, 0, data.saveData.description());
        } catch (Throwable t) {
            throw new InvalidSavableElementException("Could not mappify field " + data.getFieldName() + " in " + context.getType(), t);
        }
    }

    private WrappedObject mappifyValue(TypeInfo info, Object value, boolean objectifyStatics, int typingIndex, String comment) {
        if (value == null) {
            return WrappedObject.of(null, comment);
        }
        else if (shouldStoreAsReference(value, objectifyStatics)) {
            return mappifyAsReference(info, value, comment);
        }
        else if (Context.of(value).hasMappifiableTraits()) {
            WrappedObjectMap map = mappify(value);
            if (value instanceof Enum) {
                map.put(Reserved.E_NAME, WrappedObject.of(((Enum<?>) value).name()));
            }
            Class<?> trueClass = value.getClass();
            if (trueClass != info.getType()) {
                map.put(Reserved.C_NAME, WrappedObject.of(ClassToName.getName(value.getClass(), forceC_NAME)));
            }
            return WrappedObject.of(map, comment);
        }
        else if (info.isCollection()) {
            return WrappedObject.of(mappifyCollection(info, (Collection<?>) value, objectifyStatics, typingIndex), comment);
        } else if (info.isMap()) {
            return WrappedObject.of(mappifyMap(info, (Map<?, ?>) value, objectifyStatics, typingIndex), comment);
        } else if (info.isArray()) {
            return WrappedObject.of(mappifyArray(info, value, objectifyStatics, typingIndex), comment);
        }
        else {
            return WrappedObject.of(value, comment);
        }
    }

    private WrappedObject mappifyAsReference(TypeInfo info, Object value, String comment) {
        if (value instanceof Class) {
            if (info.getType() == Class.class) {
                return WrappedObject.of(ClassToName.getName((Class<?>) value, forceC_NAME), comment);
            } else {
                WrappedObjectMap map = new WrappedObjectMap();
                map.put(Reserved.C_NAME, WrappedObject.of(ClassToName.getName(value.getClass(), forceC_NAME)));
                return WrappedObject.of(map, comment);
            }
        }
        // Will always be enum.
        else {
            if(info.getType() == ((Enum<?>) value).getDeclaringClass()) {
                return WrappedObject.of(value, comment);
            }
            else{
                WrappedObjectMap map = new WrappedObjectMap();
                map.put(Reserved.C_NAME, WrappedObject.of(ClassToName.getName(value.getClass(), forceC_NAME)));
                map.put(Reserved.E_NAME, WrappedObject.of(((Enum<?>) value).name()));
                return WrappedObject.of(map,comment);
            }
        }
    }

    private WrappedObjectMap mappifyMap(TypeInfo info, Map<?,?> map, boolean raw, int typingIndex){
        Class<?> valueType = info.getTyping(typingIndex+1);
        WrappedObjectMap out = new WrappedObjectMap();
        for (Object key : map.keySet()) {
            Object val = map.get(key);
            if(val == null){
                out.put(key,WrappedObject.nullified());
            }
            else {
                if(!valueType.isAssignableFrom(val.getClass())){
                    throw new InvalidSavableElementException("Typing does not match Map generics.");
                }
                out.put(key, mappifyValue(TypeInfo.cast(info,valueType), val, raw, typingIndex + 2, null));
            }
        }
        return out;
    }

    private WrappedObjectList mappifyCollection(TypeInfo info, Collection<?> collection, boolean raw, int typingIndex) {
        Class<?> valueType = info.getTyping(typingIndex);
        WrappedObjectList out = new WrappedObjectList();
        for (Object obj : collection) {
            if(obj == null){
                out.add(WrappedObject.nullified());
            }
            else{
                if(!valueType.isAssignableFrom(obj.getClass())){
                    throw new InvalidSavableElementException("Typing does not match Collection generics. Received object of type " + obj.getClass() + " but expected " + valueType);
                }
                out.add(mappifyValue(TypeInfo.cast(info,valueType), obj, raw, typingIndex + 1, null));
            }
        }
        return out;
    }

    private WrappedObjectList mappifyArray(TypeInfo info, Object array, boolean raw, int typingIndex) {
        Class<?> valueType = info.getTyping(typingIndex);
        WrappedObjectList out = new WrappedObjectList();
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            Object obj = Array.get(array,i);
            if(obj == null){
                out.add(WrappedObject.nullified());
            }
            else{
                if(!Boxing.requireObjective(valueType).isAssignableFrom(obj.getClass())){
                    throw new InvalidSavableElementException("Typing does not match Array types. Received object of type " + obj.getClass() + " but expected " + valueType);
                }
                out.add(mappifyValue(TypeInfo.cast(info,valueType), obj, raw, typingIndex + 1, null));
            }
        }
        return out;
    }

    private Object mappifyClass(TypeInfo info, Object classObject, boolean raw){
        Context internalContext = Context.of(classObject);
        if(!raw && internalContext.hasMappifiableTraits()){
            WrappedObjectMap obj = mappify(internalContext);
            if(info.getType() != classObject){
                obj.put(Reserved.C_NAME,WrappedObject.of(Stringify.stringify(classObject),reservedComment()));
            }
            return obj;
        }
        return classObject;
    }

    private String reservedComment() {
        return applyCommentsOnReservedFields ? "DO NOT EDIT" : "";
    }

    /**
     * Initializes a new object using its generated initializer or using a default constructor, takes in an unordered map of key-arg entries.
     */
    @SuppressWarnings("all")
    public <T> @NotNull T unmappifyThroughInit(@NotNull Class<T> type, Map<Object,Object> map){
        ClassData data = ClassData.getClassData(type);
        if(data.hasInitializer()){
            return (T)data.getInitializer().constructMap(map,converter);
        }
        else{
            T t = Initializers.initialize(type);
            unmappify(t,map);
            return t;
        }
    }

    /**
     * Initializes a new object using its generated initializer or using a default constructor, takes in an ordered list of args.
     */
    @SuppressWarnings("all")
    public <T> @NotNull T unmappifyThroughInit(@NotNull Class<T> type, List<Object> orderedArgs){
        ClassData data = ClassData.getClassData(type);
        if(data.hasInitializer()){
            return (T)data.getInitializer().constructList(orderedArgs,converter);
        }
        else{
            throw new IllegalArgumentException(type + " does not have a generated initializer");
        }
    }

    /**
     * Initializes a new object using its generated initializer or using a default constructor, takes in one arg.
     */
    @SuppressWarnings("all")
    public <T> @NotNull T unmappifyThroughInit(@NotNull Class<T> type, Object monoArg){
        ClassData data = ClassData.getClassData(type);
        if(data.hasInitializer()){
            return (T)data.getInitializer().construct(monoArg,converter);
        }
        else{
            throw new IllegalArgumentException(type + " does not have a generated initializer");
        }
    }

    /**
     * Writes a map's values to an objects Fields marked with {@link com.vicious.persist.annotations.Save}
     * @param writeTarget the object to write to.
     * @param map the map to take data from.
     */
    public void unmappify(Object writeTarget, WrappedObjectMap map){
        this.unmappify(writeTarget,map.unwrap());
    }

    /**
     * Writes a map's values to an objects Fields marked with {@link com.vicious.persist.annotations.Save}
     * @param writeTarget the object to write to.
     * @param map the map to take data from.
     */
    public void unmappify(Object writeTarget, Map<Object,Object> map){
        unmappify(Context.of(writeTarget),map);
    }


    private void unmappify(Context context, Map<Object,Object> map){
        if(context.hasTransformations()){
            context.transform(map);
        }
        Set<FieldData<?>> required = context.data.copyRequired(context);
        for (Object o : map.keySet()) {
            try {
                context.whenPresent(Stringify.stringify(o), fieldData -> {
                    unmappify(fieldData, map.get(o), context);
                    if(!required.isEmpty()) {
                        required.remove(fieldData);
                    }
                });
            } catch (Throwable t) {
                throw t;
            }
        }
        if(!required.isEmpty()){
            FieldData<?> zero = required.iterator().next();
            throw new NoValuePresentException("Did not find required value of key " + zero.getName() + " in the provided map!");
        }
    }

    private void unmappify(FieldData<?> data, Object parsedValue, Context context) {
        try {
            Object unmapped = unmappifyValue(data, data.get(context), data.objectified(), parsedValue,0);
            data.set(context,unmapped);
        } catch (Throwable t){
            throw new InvalidSavableElementException("Could not unmappify field " + data.getFieldName() + " in " + context.getType(),t);
        }
    }

    /**
     * Converts a mappified value into an unmappified one.
     * @param info TypeInfo containing the expected type of output and its generics.
     * @param currentValue the current value. Can be null.
     * @param objectifyStatics when true classes are stored in reference format.
     * @param parsedValue the mappified value.
     * @param typingIndex the typing index to get generics from.
     * @return an unmappified Object.
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    private Object unmappifyValue(TypeInfo info, @Nullable Object currentValue, boolean objectifyStatics, Object parsedValue, int typingIndex){
        if(parsedValue == null){
            return null;
        }
        if(info.getType() == parsedValue.getClass()){
            return parsedValue;
        }
        else if(parsedValue instanceof Collection){
            if(info.isCollection()){
                return unmappifyCollection(info, (Collection<Object>) Initializers.ensureNotNull(currentValue,info.getType()) ,(Collection<?>) parsedValue, objectifyStatics, typingIndex);
            }
            else if(info.isArray()){
                return unmappifyArray(info, (Collection<?>)parsedValue, objectifyStatics, typingIndex);
            }
            else{
                throw new InvalidValueException("Parsed value is a Collection but the expected type is of " + info.getType() + " which cannot be converted to from a collection.");
            }
        }
        else if(parsedValue instanceof Map){
            if(info.isMap()){
                return unmappifyMap(info, (Map<Object,Object>) Initializers.ensureNotNull(currentValue,info.getType()), (Map<?,?>) parsedValue, objectifyStatics,typingIndex);
            }
            // Force return the custom reconstructor deserializer
            else if(Initializers.useCustomReconstructor(info.getType())) {
                return ClassData.getClassData(info.getType()).getInitializer().constructMap((Map<Object,Object>)parsedValue,converter);
            }
            else {
                Map<?, ?> map = (Map<?, ?>) parsedValue;
                //Change target type to the appropriate type and obtain enum instances.
                if (map.containsKey(Reserved.C_NAME)) {
                    info = TypeInfo.cast(info, ClassToName.get(map.get(Reserved.C_NAME).toString()));
                }
                if (map.containsKey(Reserved.E_NAME)) {
                    if (!info.getType().isEnum() && info.getType().getSuperclass().isEnum()) {
                        currentValue = Enum.valueOf((Class) info.getType().getSuperclass(), map.get(Reserved.E_NAME).toString());
                    } else {
                        currentValue = Enum.valueOf((Class) info.getType(), map.get(Reserved.E_NAME).toString());
                    }
                } else {
                    currentValue = Initializers.enforce(info.getType(), currentValue);
                }
                if(shouldStoreAsReference(info,objectifyStatics)){
                   return currentValue;
                }
                if (Context.of(info.getType()).hasMappifiableTraits(info.getType() == Class.class)) {
                    currentValue = Initializers.ensureNotNull(currentValue, info.getType());
                    unmappify(currentValue, (Map<Object, Object>) parsedValue);
                    return currentValue;
                } else {
                    throw new InvalidValueException("Parsed value is a Map but the expected type is of " + info.getType() + " which cannot be converted to from a map.");
                }
            }
        }
        return Stringify.objectify(info.getType(),parsedValue.toString());
    }

    private Map<Object,Object> unmappifyMap(TypeInfo info, Map<Object,Object> currentValue, Map<?,?> parsedMap, boolean raw, int typingIndex){
        Class<?> keyType = info.getTyping(typingIndex);
        Class<?> valueType = info.getTyping(typingIndex+1);
        currentValue.clear();
        for (Object key : parsedMap.keySet()) {
            Object val = parsedMap.get(key);
            currentValue.put(unmappifyValue(TypeInfo.cast(info, keyType), null, raw, key, typingIndex+2),
                    unmappifyValue(TypeInfo.cast(info, valueType), null, raw, val, typingIndex+2));
        }
        return currentValue;
    }

    private Collection<?> unmappifyCollection(TypeInfo info, Collection<Object> currentValue, Collection<?> parsedCollection, boolean raw, int typingIndex) {
        Class<?> valueType = info.getTyping(typingIndex);
        currentValue.clear();
        for (Object obj : parsedCollection) {
            currentValue.add(unmappifyValue(TypeInfo.cast(info,valueType),null, raw, obj,typingIndex+1));
        }
        return currentValue;
    }

    private Object unmappifyArray(TypeInfo info, Collection<?> parsedCollection, boolean raw, int typingIndex) {
        Class<?> valueType = info.getTyping(typingIndex);
        Object arrayOut = Array.newInstance(valueType, parsedCollection.size());
        int i = 0;
        for (Object obj : parsedCollection) {
            Boxing.arraySet(arrayOut,i,unmappifyValue(TypeInfo.cast(info,valueType),null,raw,obj,typingIndex+1));
            i++;
        }
        return arrayOut;
    }

    private boolean shouldStoreAsReference(TypeInfo info, boolean objectifyStatics){
        if(info.getType() == Class.class || info.getType().isEnum()){
            return !objectifyStatics;
        }
        return false;
    }

    private boolean shouldStoreAsReference(Object obj, boolean objectifyStatics){
        if(obj instanceof Class || obj instanceof Enum){
            return !objectifyStatics;
        }
        return false;
    }
}
