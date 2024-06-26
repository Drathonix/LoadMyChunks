package com.vicious.loadmychunks.common.integ;

import dev.architectury.platform.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class Integrations {
    public static void whenModLoaded(String modid, Runnable runnable){
        if(Platform.isModLoaded(modid)){
            runnable.run();
        }
    }

    public static void invokeWhenLoaded(String modid, String cls, String method, Class<?>[] params, Object... vals){
        whenModLoaded(modid, () -> {
            try {
                Class.forName(cls).getMethod(method,params).invoke(null,vals);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}