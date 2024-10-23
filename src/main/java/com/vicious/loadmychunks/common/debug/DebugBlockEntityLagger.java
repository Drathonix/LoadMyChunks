package com.vicious.loadmychunks.common.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
//? if <=1.16.5
/*import net.minecraft.world.level.block.entity.TickableBlockEntity;*/
import net.minecraft.world.level.block.state.BlockState;

class DebugBlockEntityLagger extends BlockEntity
    //? if <=1.16.5
        /*implements TickableBlockEntity*/
    {
    //? if >1.16.5 {
    public DebugBlockEntityLagger(BlockPos blockPos, BlockState blockState) {
        super(LoadMyChunksDebug.laggerBlockEntity.get(), blockPos, blockState);
    }
    //?}
    //? if <=1.16.5 {
    /*DebugBlockEntityLagger() {
        super(DebugLoadMyChunks.laggerBlockEntity.get());
    }
    *///?}

    void serverTick(){
        try {
            Thread.sleep(LoadMyChunksDebug.laggerMsSleep);
        } catch (InterruptedException ignored) {}
    }


    //? if <=1.16.5 {
    /*@Override
    public void tick() {
        serverTick();
    }
    *///?}
}
