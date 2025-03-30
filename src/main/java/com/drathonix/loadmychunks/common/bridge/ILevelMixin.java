package com.drathonix.loadmychunks.common.bridge;

//? if >1.16.5
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public interface ILevelMixin {
    //? if <=1.16.5 {
    /*void loadMyChunks$removeTicker(BlockEntity tickingBlockEntity);*/
    //?} else {
    void loadMyChunks$removeTicker(TickingBlockEntity tickingBlockEntity);
    //?}
}
