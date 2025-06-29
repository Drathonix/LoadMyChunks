package com.vicious.persist.util;

import java.util.function.Consumer;

public class ReflectionHelper {
    /**
     * Goes through a class' hierarchy and executes some arbitrary code.
     * @param cls the child class
     * @param consumer the code to execute on that class' hierarchy
     */
    public static void forEach(Class<?> cls, Consumer<Class<?>> consumer){
        if(cls != null){
            consumer.accept(cls);
            for (Class<?> anInterface : cls.getInterfaces()) {
                forEach(anInterface, consumer);
            }
            forEach(cls.getSuperclass(), consumer);
        }
    }
}
