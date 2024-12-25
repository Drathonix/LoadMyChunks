package persist.mappify;

import com.vicious.persist.except.InvalidSavableElementException;
import com.vicious.persist.except.InvalidValueException;
import com.vicious.persist.io.writer.wrapped.WrappedObject;
import com.vicious.persist.io.writer.wrapped.WrappedObjectList;
import com.vicious.persist.io.writer.wrapped.WrappedObjectMap;
import com.vicious.persist.mappify.ClassToName;
import com.vicious.persist.mappify.Context;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.mappify.reflect.TypeInfo;
import com.vicious.persist.mappify.registry.Initializers;
import com.vicious.persist.mappify.registry.Reserved;
import com.vicious.persist.mappify.registry.Stringify;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class Mappifier {
    public static final Mappifier DEFAULT = new Mappifier();

    public static Mappifier create(){
        return new Mappifier();
    }

    private boolean applyCommentsOnReservedFields = false;
    private boolean forceC_NAME = false;
    private Mappifier(){}

    public Mappifier applyCommentsOnReservedFields(boolean setting){
        this.applyCommentsOnReservedFields = setting;
        return this;
    }

    public Mappifier forceC_NAME(boolean setting){
        this.forceC_NAME = setting;
        return this;
    }

    public WrappedObjectMap mappify(Object object){
        return mappify(com.vicious.persist.mappify.Context.of(object));
    }

    private WrappedObjectMap mappify(com.vicious.persist.mappify.Context context) {
        WrappedObjectMap output = new WrappedObjectMap();
        context.forEach(fieldData -> {
            output.put(fieldData.getName(),mappify(fieldData, context, fieldData.isRaw()));
        });
        return output;
    }

    private WrappedObject mappify(FieldData<?> data, com.vicious.persist.mappify.Context context, boolean raw) {
        try {
            return mappifyValue(data, data.get(context), raw,0, data.saveData.description());
        } catch (Throwable t){
            throw new InvalidSavableElementException("Could not mappify field " + data.getFieldName() + " in " + context.getType(),t);
        }
    }

    private WrappedObject mappifyValue(TypeInfo info, Object value, boolean raw, int typingIndex, String comment){
        if(value == null || raw && !info.isDataStructure()){
            return WrappedObject.of(value,comment);
        }
        if(info.isClass()){
            return WrappedObject.of(mappifyClass(info,value,raw),comment);
        }
        else if(info.isCollection()){
            return WrappedObject.of(mappifyCollection(info, (Collection<?>) value,raw,typingIndex),comment);
        }
        else if(info.isMap()){
            return WrappedObject.of(mappifyMap(info, (Map<?,?>) value,raw,typingIndex),comment);
        }

        if(com.vicious.persist.mappify.Context.of(value).hasMappifiableTraits()){
            WrappedObjectMap map = mappify(value);
            if(value instanceof Enum){
                map.put(Reserved.E_NAME,WrappedObject.of(((Enum<?>)value).name()));
            }
            Class<?> trueClass = value instanceof Enum ? ((Enum<?>) value).getDeclaringClass() : value.getClass();
            if(trueClass != info.getType()){
                map.put(Reserved.C_NAME, WrappedObject.of(com.vicious.persist.mappify.ClassToName.getName(value.getClass(),forceC_NAME)));
            }
            return WrappedObject.of(map,comment);
        }
        else{
            Class<?> trueClass = value instanceof Enum ? ((Enum<?>) value).getDeclaringClass() : value.getClass();
            if(value instanceof Enum && trueClass != info.getType()){
                WrappedObjectMap map = new WrappedObjectMap();
                map.put(Reserved.E_NAME,WrappedObject.of(((Enum<?>)value).name()));
                map.put(Reserved.C_NAME, WrappedObject.of(com.vicious.persist.mappify.ClassToName.getName(value.getClass(),forceC_NAME)));
                return WrappedObject.of(map,comment);
            }
           // else if(value instanceof Enum){
            //    return WrappedObject.of(((Enum<?>)value).name(),comment);
            //}
            else {
                return WrappedObject.of(value, comment);
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

    private Object mappifyClass(TypeInfo info, Object classObject, boolean raw){
        com.vicious.persist.mappify.Context internalContext = com.vicious.persist.mappify.Context.of(classObject);
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





    public void unmappify(Object writeTarget, WrappedObjectMap map){
        this.unmappify(writeTarget,map.unwrap());
    }

    public void unmappify(Object writeTarget, Map<Object,Object> map){
        unmappify(com.vicious.persist.mappify.Context.of(writeTarget),map);
    }

    private void unmappify(com.vicious.persist.mappify.Context context, Map<Object,Object> map){
        for (Object o : map.keySet()) {
            try {
                context.whenPresent(Stringify.stringify(o), fieldData -> {
                    unmappify(fieldData, map.get(o), context);
                });
            } catch (Throwable t){
                System.err.println("Map data: " + map);
                throw t;
            }
        }
    }

    private void unmappify(FieldData<?> data, Object parsedValue, com.vicious.persist.mappify.Context context) {
        try {
            Object unmapped = unmappifyValue(data, data.get(context), data.isRaw(), parsedValue,0);
            data.set(context,unmapped);
        } catch (Throwable t){
            throw new InvalidSavableElementException("Could not unmappify field " + data.getFieldName() + " in " + context.getType(),t);
        }
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private Object unmappifyValue(TypeInfo info, @Nullable Object currentValue, boolean raw, Object parsedValue, int typingIndex){
        if(parsedValue == null){
            return null;
        }
        if(info.isCollection()){
            return unmappifyCollection(info, (Collection<Object>) Initializers.ensureNotNull(currentValue,info.getType()) ,(Collection<?>) parsedValue, raw, typingIndex);
        }
        else if(info.isMap()){
            return unmappifyMap(info, (Map<Object,Object>) Initializers.ensureNotNull(currentValue,info.getType()), (Map<?,?>) parsedValue, raw,typingIndex);
        }
        //Force return the custom reconstructor deserializer
        if(Initializers.useCustomReconstructor(info.getType())) {
            return Initializers.construct((Map<Object,Object>)parsedValue,info.getType());
        }
        if(parsedValue instanceof Map<?,?>) {
            Map<?, ?> map = (Map<?, ?>) parsedValue;
            //Change target type to the appropriate type and obtain enum instances.
            if (map.containsKey(Reserved.C_NAME)) {
                info = TypeInfo.cast(info, ClassToName.get(map.get(Reserved.C_NAME).toString()));
            }
            if (map.containsKey(Reserved.E_NAME)) {
                if(!info.getType().isEnum() && info.getType().getSuperclass().isEnum()) {
                    currentValue = Enum.valueOf((Class) info.getType().getSuperclass(), map.get(Reserved.E_NAME).toString());
                }
                else {
                    currentValue = Enum.valueOf((Class) info.getType(), map.get(Reserved.E_NAME).toString());
                }
            }
            else{
                currentValue = Initializers.enforce(info.getType(),currentValue);
            }
        }
        if (Context.of(info.getType()).hasMappifiableTraits(info.getType() == Class.class) && !raw) {
            if(parsedValue instanceof Map<?,?>) {
                currentValue = Initializers.ensureNotNull(currentValue, info.getType());
                unmappify(currentValue, (Map<Object, Object>) parsedValue);
                return currentValue;
            }
            else{
                throw new InvalidValueException("Provided value: " + parsedValue + " is not a Map! Cannot unmappify mappifiable object!");
            }
        }
        if(info.getType() == parsedValue.getClass()){
            return parsedValue;
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
}
