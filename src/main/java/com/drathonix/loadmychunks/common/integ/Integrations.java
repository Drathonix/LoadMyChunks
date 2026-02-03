package com.drathonix.loadmychunks.common.integ;

//? if >1.16.5 {
import dev.architectury.platform.Platform;
//?} else {
/*import me.shedaniel.architectury.platform.Platform;
*///?}
//? if fabric {
/*import net.fabricmc.api.EnvType;
*///?}
import java.lang.reflect.InvocationTargetException;

/**
 * Executes code only if a specific condition is met. Will not load the class otherwise.
 */
public class Integrations {
    /**
     * Runs a runnable only if the mod is present.
     * @param modid the mod.
     * @param runnable the arbitrary runnable.
     */
    public static void whenModLoaded(String modid, Runnable runnable){
        if(Platform.isModLoaded(modid)){
            runnable.run();
        }
    }

    /**
     * Executes code only if a specific mode is loaded.
     * @param modid the mod.
     * @param cls the canonical class name of the target executable class.
     * @param method a static method present in cls.
     * @param params the method parameter types.
     * @param vals the parameter arguments.
     */
    public static void invokeWhenLoaded(String modid, String cls, String method, Class<?>[] params, Object... vals){
        whenModLoaded(modid, () -> {
            try {
                Class.forName(cls).getMethod(method,params).invoke(null,vals);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Executes code only on the server side.
     * @param runnable the arbitrary code to execute.
     */
    public static void invokeServer(Runnable runnable) {
        //? if neoforge || forge {
        if(Platform.getEnv().isDedicatedServer()){
            runnable.run();
        }
        //?} else {
        /*if(Platform.getEnv() == EnvType.SERVER){
            runnable.run();
        }
        *///?}
    }
}
