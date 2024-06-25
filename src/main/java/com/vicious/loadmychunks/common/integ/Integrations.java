package com.vicious.loadmychunks.common.integ;

import dev.architectury.platform.Platform;

public class Integrations {
    public static void whenModLoaded(String modid, Runnable runnable){
        if(Platform.isModLoaded(modid)){
            runnable.run();
        }
    }
}