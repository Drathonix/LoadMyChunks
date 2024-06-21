package com.vicious.loadmychunks.neoforge.system.loaders;

import com.vicious.loadmychunks.neoforge.ForgeLoaderTypes;
import com.vicious.loadmychunks.system.loaders.PlacedChunkLoader;
import net.minecraft.resources.ResourceLocation;

public class MekanismChunkLoader extends PlacedChunkLoader {
    @Override
    public ResourceLocation getTypeId() {
        return ForgeLoaderTypes.mekanismPlaced;
    }
}
