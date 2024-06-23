package com.vicious.loadmychunks.common.util;

import com.vicious.loadmychunks.common.LoadMyChunks;
import net.minecraft.resources.ResourceLocation;

public class ModResource {
    public static ResourceLocation of(String string){
        //? if <1.20.7
        /*return new ResourceLocation(LoadMyChunks.MOD_ID,string);*/
        //? if >1.20.6
        return ResourceLocation.fromNamespaceAndPath(LoadMyChunks.MOD_ID,string);
    }

    public static ResourceLocation parse(String string) {
        //? if <1.20.7
        /*return new ResourceLocation(string);*/
        //? if >1.20.6
        return ResourceLocation.tryParse(string);
    }
}
