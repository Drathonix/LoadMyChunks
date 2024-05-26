package com.vicious.loadmychunks.debug;

import net.minecraft.world.level.block.entity.BlockEntity;

class DebugBlockEntityLagger extends BlockEntity {

    DebugBlockEntityLagger() {
        super(DebugLoadMyChunks.laggerBlockEntity.get());
    }

    void serverTick(){
        try {
            Thread.sleep(DebugLoadMyChunks.laggerMsSleep);
        } catch (InterruptedException ignored) {}
    }
}
