package com.vicious.persist.mappify;

import com.vicious.persist.annotations.C_NAME;
import com.vicious.persist.except.InvalidAnnotationException;
import com.vicious.persist.mappify.registry.Stringify;

import java.util.HashMap;
import java.util.Map;

public class ClassToName {
    private static final Map<String,Class<?>> map = new HashMap<>();

    public static void add(Class<?> cls){
        String name = getName(cls,true);
        if(!map.containsKey(name)) {
            map.put(name, cls);
        }
        else{
            throw new InvalidAnnotationException("Attempted to register multiple classes as: " + name + ", " + cls.getName() + " conflicted with: " + map.get(name));
        }
    }

    public static Class<?> get(String value){
        return map.computeIfAbsent(value,(k)-> {
            try {
                return Class.forName(value);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not find class with C_NAME: " + value);
            }
        });
    }

    public static String getName(Class<?> cls, boolean forceC_NAME) {
        C_NAME annotation = cls.getAnnotation(C_NAME.class);
        if(annotation != null){
            return annotation.value();
        }
        else{
            if(forceC_NAME) {
                throw new RuntimeException("Missing @C_NAME annotation on class " + cls.getName());
            }
            else{
                return Stringify.stringify(cls);
            }
        }
    }
}
