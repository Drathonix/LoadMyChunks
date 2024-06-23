package com.vicious.loadmychunks.common.bridge;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ILevelMixin {
    void loadMyChunks$removeTicker(BlockEntity tickingBlockEntity);
}
