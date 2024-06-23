package com.vicious.loadmychunks.common.debug;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

class DebugBlockEntityLagger extends BlockEntity
    //? if <=1.16.5
        implements TickableBlockEntity
    {

    DebugBlockEntityLagger() {
        super(DebugLoadMyChunks.laggerBlockEntity.get());
    }

    void serverTick(){
        try {
            Thread.sleep(DebugLoadMyChunks.laggerMsSleep);
        } catch (InterruptedException ignored) {}
    }


    //? if <=1.16.5 {
    @Override
    public void tick() {
        serverTick();
    }
    //?}
}
