package com.vicious.loadmychunks.util;

import com.vicious.loadmychunks.LoadMyChunks;
import net.minecraft.resources.ResourceLocation;

public class ModResource extends ResourceLocation {
    public ModResource(String string) {
        super(LoadMyChunks.MOD_ID,string);
    }
}
