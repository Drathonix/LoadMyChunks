package com.vicious.loadmychunks.common.bridge;

import net.minecraft.world.level.block.entity.BlockEntity;
//? if >1.16.5
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public interface ILevelMixin {
    //? if <=1.16.5
    /*void loadMyChunks$removeTicker(BlockEntity tickingBlockEntity);*/
    //? if >1.16.5
    void loadMyChunks$removeTicker(TickingBlockEntity tickingBlockEntity);
}
