package com.vicious.loadmychunks.block;


import com.mojang.serialization.MapCodec;
import com.vicious.loadmychunks.block.blockentity.BlockEntityLagometer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLagometer extends BaseEntityBlock {
    public static final MapCodec<BlockLagometer> CODEC = simpleCodec(BlockLagometer::new);
    public BlockLagometer(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityLagometer(blockPos,blockState);
    }
}
