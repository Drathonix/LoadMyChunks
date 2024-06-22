package com.vicious.loadmychunks.block.blockentity;

import com.vicious.loadmychunks.registry.LMCContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityLagometer extends BlockEntity {
    public BlockEntityLagometer(BlockPos blockPos, BlockState blockState) {
        super(LMCContent.chunkLoaderBlockEntity.get(), blockPos, blockState);
    }
}
