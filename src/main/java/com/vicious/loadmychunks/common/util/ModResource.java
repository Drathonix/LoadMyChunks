package com.vicious.loadmychunks.common.util;

import com.vicious.loadmychunks.common.LoadMyChunks;
import net.minecraft.resources.ResourceLocation;

public class ModResource {
    public static ResourceLocation of(String string){
        //? if <1.20.6 {
        return new ResourceLocation(LoadMyChunks.MOD_ID,string);
        //?}
    }
}
