package com.vicious.loadmychunks.common.debug;


import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.jetbrains.annotations.Nullable;

class DebugBlockLagger extends BaseEntityBlock {
    public DebugBlockLagger(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public DebugBlockEntityLagger newBlockEntity(BlockGetter bg) {
        return new DebugBlockEntityLagger();
    }
}
