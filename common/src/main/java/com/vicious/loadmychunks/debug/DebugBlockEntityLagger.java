package com.vicious.loadmychunks.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

class DebugBlockEntityLagger extends BlockEntity {

    DebugBlockEntityLagger(BlockPos blockPos, BlockState blockState) {
        super(DebugLoadMyChunks.laggerBlockEntity.get(), blockPos, blockState);
    }

    void serverTick(){
        try {
            Thread.sleep(DebugLoadMyChunks.laggerMsSleep);
        } catch (InterruptedException ignored) {}
    }
}
