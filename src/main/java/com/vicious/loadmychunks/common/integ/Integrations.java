package com.vicious.loadmychunks.common.integ;

//? if >1.16.5
import dev.architectury.platform.Platform;
//? if fabric
/*import net.fabricmc.api.EnvType;*/
//? if <=1.16.5
/*import me.shedaniel.architectury.platform.Platform;*/

import java.lang.reflect.InvocationTargetException;

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

    //TODO: make less scuffed.
    public static void invokeServer(Runnable runnable) {
        //? if neoforge || forge {
        if(Platform.getEnv().isDedicatedServer()){
            runnable.run();
        }
        //?}
        //? if fabric {
        /*if(Platform.getEnv() == EnvType.SERVER){
            runnable.run();
        }
        *///?}
    }
}
