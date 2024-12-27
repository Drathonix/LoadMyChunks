package com.vicious.persist.mappify.registry;

import com.vicious.persist.except.CannotInitializeException;
import com.vicious.persist.util.ClassMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Initializers {
    private static final ClassMap<Supplier<?>> initializers = new ClassMap<>();
    private static final ClassMap<Function<Map<Object,Object>,?>> constructors = new ClassMap<>();

    public static <T> T ensureNotNull(Object value, Class<T> type){
        if(value == null){
            return initialize(type);
        }
        else{
            return (T) value;
        }
    }


    public static  <T> T initialize(Class<T> type){
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
    public static  <T> void registerCustomConstructor(Class<T> cls, Function<Map<Object,Object>,T> factory){
        constructors.put(cls, factory);
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
        return constructors.containsKey(type);
    }

    public static Object construct(Map<Object,Object> parsedValue, Class<?> type) {
        if(useCustomReconstructor(type)) {
            return constructors.get(type).apply(parsedValue);
        }
        else{
            throw new CannotInitializeException("No custom constructor registered for " + type.getName());
        }
    }
}
