package com.vicious.persist.mappify.registry;

import com.vicious.persist.annotations.Save;
import com.vicious.persist.except.CannotInitializeException;
import com.vicious.persist.except.InvalidAnnotationException;
import com.vicious.persist.mappify.reflect.ClassData;
import com.vicious.persist.mappify.reflect.FieldData;
import com.vicious.persist.util.ClassMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Initializers {
    private static final ClassMap<Supplier<?>> initializers = new ClassMap<>();

    public static <T> T ensureNotNull(Object value, Class<T> type){
        if(value == null){
            return initialize(type);
        }
        else{
            return (T) value;
        }
    }


    public static <T> T initialize(Class<T> type){
        if(!initializers.containsKey(type)){
            try {
                Constructor<T> constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                register(type,()-> {
                    try {
                        return constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new CannotInitializeException("Constructor for " + type.getName() + " threw an exception", e);
                    }
                });
            } catch (NoSuchMethodException e) {
                throw new CannotInitializeException("No default constructor exists for " + type.getName());
            }
        }
        return (T) initializers.get(type).get();
    }

    public static  <T> void register(Class<T> cls, Supplier<T> supplier){
        initializers.put(cls, supplier);
    }

    public static <T> T enforce(Class<T> type, Object value) {
        if(value == null){
            return initialize(type);
        }
        else if(value.getClass() != type){
            return initialize(type);
        }
        return (T) value;
    }

    public static boolean useCustomReconstructor(Class<?> type) {
        return ClassData.getClassData(type).hasInitializer();
    }

    public static <T> CustomConstructor<?> tryGenerateCustomReconstructorFor(Class<T> cls, ClassData data){
        l1: for (Constructor<?> declaredConstructor : cls.getDeclaredConstructors()) {
            if(declaredConstructor.isAnnotationPresent(Save.Constructor.class)){
                return generateCustomReconstructorFor(declaredConstructor,data);
            }
            if(declaredConstructor.getParameterCount() > 0){
                for (Parameter parameter : declaredConstructor.getParameters()) {
                    Save save = parameter.getAnnotation(Save.class);
                    if(save == null){
                        continue l1;
                    }
                    return generateCustomReconstructorFor(declaredConstructor,data);
                }
            }
        }
        return null;
    }

    private static <T> CustomConstructor<?> generateCustomReconstructorFor(Constructor<T> constructor, ClassData data) {
        final List<FieldData<?>> parameters = new ArrayList<>();
        if(constructor.isAnnotationPresent(Save.Constructor.class)){
            Save.Constructor cnst = constructor.getAnnotation(Save.Constructor.class);
            if(cnst.value().length != constructor.getParameterCount()){
                throw new InvalidAnnotationException("Constructor in " + constructor.getDeclaringClass() + " annotated with @Save.Constructor does not have the same number of parameters as the Save.Constructor$value() array.");
            }
            for (String name : cnst.value()) {
                FieldData<?> fieldData = data.getField(name);
                if (fieldData == null) {
                    throw new InvalidAnnotationException("No @Save field with name " + name + " for constructor parameter in " + constructor.getDeclaringClass() + " constructor.");
                }
                parameters.add(fieldData);
            }
        }
        else {
            for (Parameter parameter : constructor.getParameters()) {
                String name = getName(parameter);
                FieldData<?> fieldData = data.getField(name);
                if (fieldData == null) {
                    throw new InvalidAnnotationException("No @Save field with name " + name + " for constructor parameter " + parameter.getName() + " in " + constructor.getDeclaringClass());
                }
                parameters.add(fieldData);
            }
        }

        constructor.setAccessible(true);
        return new CustomConstructor<>((map,converter)->{
            if(map.size() < parameters.size()){
                throw new CannotInitializeException("Provided map of arguments has " + map.size() + " arguments but needs to have " + parameters.size());
            }
            else {
                Object[] args = new Object[parameters.size()];
                for (int i = 0; i < parameters.size(); i++) {
                    FieldData<?> fieldData = parameters.get(i);
                    for (String s : data.getKeysOfField(fieldData)) {
                        args[i] = map.get(s);
                        if(args[i] != null){
                            args[i] = ((DynamicConverter<Object>)converter).convert(fieldData,i,args[i]);
                            break;
                        }
                    }
                }
                try {
                    if(args.length < parameters.size()){
                    }
                    return constructor.newInstance(args);
                } catch (Throwable e) {
                    for (int i = 0; i < args.length; i++) {
                        if(args[i] == null) {
                            throw new CannotInitializeException("Provided map is missing constructor argument: " + parameters.get(i).getName());
                        }
                    }
                    throw new CannotInitializeException(e);
                }
            }
        },(list,converter)->{
            if(list.size() < parameters.size()){
                throw new CannotInitializeException("Provided list of arguments has " + list.size() + " arguments but needs to have " + parameters.size());
            }
            else{
                try {
                    Object[] args = new Object[list.size()];
                    for (int i = 0; i < parameters.size(); i++) {
                        FieldData<?> fieldData = parameters.get(i);
                        args[i] = ((DynamicConverter<Object>)converter).convert(fieldData,i,list.get(i));
                    }
                    return constructor.newInstance(args);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new CannotInitializeException(e);
                }
            }
        });
    }

    private static String getName(Parameter parameter) {
        Save anno = parameter.getAnnotation(Save.class);
        if(anno != null){
            if(anno.value().isEmpty()){
                if(parameter.isNamePresent()){
                    return parameter.getName();
                }
                else{
                    throw new InvalidAnnotationException("Cannot generate constructor, missing significant metadata providing argument names. This is critical to ordering. Mark all parameters with @Save(name = \"<name here>\")");
                }
            }
            return anno.value();
        }
        else{
            if(parameter.isNamePresent()){
                return parameter.getName();
            }
            else{
                throw new InvalidAnnotationException("Cannot generate constructor, missing significant metadata providing argument names. This is critical to ordering. Mark all parameters with @Save(name = \"<name here>\")");
            }
        }
    }

    public static boolean canGenerateInitializerFor(Class<?> cls) {
        l1: for (Constructor<?> declaredConstructor : cls.getDeclaredConstructors()) {
            if(declaredConstructor.isAnnotationPresent(Save.Constructor.class)){
                return true;
            }
            if(declaredConstructor.getParameterCount() > 0){
                for (Parameter parameter : declaredConstructor.getParameters()) {
                    Save save = parameter.getAnnotation(Save.class);
                    if(save == null){
                        continue l1;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static class CustomConstructor<T> {
        private final BiFunction<Map<Object,Object>,DynamicConverter<?>,T> mapConstructor;
        private final BiFunction<List<Object>,DynamicConverter<?>,T> listConstructor;

        private CustomConstructor(BiFunction<Map<Object, Object>, DynamicConverter<?>, T> mapConstructor, BiFunction<List<Object>, DynamicConverter<?>, T> listConstructor) {
            this.mapConstructor = mapConstructor;
            this.listConstructor = listConstructor;
        }

        public T constructMap(Map<Object,Object> map, DynamicConverter<?> converter){
            return mapConstructor.apply(map,converter);
        }

        public T constructList(List<Object> list, DynamicConverter<?> converter){
            return listConstructor.apply(list,converter);
        }

        public T construct(Object obj, DynamicConverter<?> converter) {
            List<Object> list = new LinkedList<>();
            list.add(obj);
            return constructList(list,converter);
        }
    }

    /**
     * Used in custom constructors to convert parameters from some parsed form to a specific java object.
     */
    @FunctionalInterface
    public interface DynamicConverter<T> {
        /**
         * Converts a parsed object to its required form in the constructor.
         * @param fieldData the parameter's {@link Save} field.
         * @param paramaterIndex the index of the parameter in the constructor.
         * @param parsed the parsed value.
         * @return the object ready for construction.
         */
        Object convert(@NotNull FieldData<?> fieldData, int paramaterIndex, @Nullable T parsed);
    }
}
