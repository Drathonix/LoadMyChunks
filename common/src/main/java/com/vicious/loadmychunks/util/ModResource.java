package com.vicious.loadmychunks.util;

import com.vicious.loadmychunks.LoadMyChunks;
import net.minecraft.resources.ResourceLocation;

public class ModResource {
    public static ResourceLocation of(String string) {
        return ResourceLocation.fromNamespaceAndPath(LoadMyChunks.MOD_ID,string);
    }
}
