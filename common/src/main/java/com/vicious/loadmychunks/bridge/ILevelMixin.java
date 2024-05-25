package com.vicious.loadmychunks.bridge;

import net.minecraft.world.level.block.entity.TickingBlockEntity;

public interface ILevelMixin {
    void loadMyChunks$removeTicker(TickingBlockEntity tickingBlockEntity);
}
