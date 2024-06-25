package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class TurtleChunkLoader extends PlacedChunkLoader {
    public TurtleChunkLoader() {}
    public TurtleChunkLoader(BlockPos pos) {
        super(pos);
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypes.CCT_TURTLE_LOADER;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
    }

    public TurtleChunkLoader move(BlockPos newPosition) {
        return new TurtleChunkLoader(newPosition);
    }
}
