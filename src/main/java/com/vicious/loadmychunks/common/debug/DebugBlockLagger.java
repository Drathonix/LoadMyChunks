package com.vicious.loadmychunks.common.debug;


//? if >1.20.3
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
//? if >1.16.5
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

class DebugBlockLagger extends BaseEntityBlock {
    public DebugBlockLagger(Properties properties) {
        super(properties);
    }

    //? if <=1.16.5 {
    /*@Nullable
    @Override
    public DebugBlockEntityLagger newBlockEntity(BlockGetter bg) {
        return new DebugBlockEntityLagger();
    }
    *///?}

    //? if >1.16.5 {
    @Nullable
    @Override
    public DebugBlockEntityLagger newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DebugBlockEntityLagger(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return createTickerHelper(blockEntityType, LoadMyChunksDebug.laggerBlockEntity.get(), (level1, blockPos, blockState1, blockEntity) -> {
                try {
                    blockEntity.serverTick();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        return null;
    }
    //?}

    //? if >1.20.3 {
    public static final MapCodec<DebugBlockLagger> CODEC = simpleCodec(DebugBlockLagger::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}
}
