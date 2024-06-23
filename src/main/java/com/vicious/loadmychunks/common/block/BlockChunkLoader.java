package com.vicious.loadmychunks.common.block;


import com.mojang.serialization.MapCodec;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockChunkLoader extends BaseEntityBlock {
    //? if >1.20.3 {
    public static final MapCodec<BlockChunkLoader> CODEC = simpleCodec(BlockChunkLoader::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    public BlockChunkLoader(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if(livingEntity != null){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof BlockEntityChunkLoader)
            ((BlockEntityChunkLoader)entity).setOwner(livingEntity.getUUID());
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    //? if >1.16.5 {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityChunkLoader(blockPos,blockState);
    }
    //?}

    //? if <=1.16.5 {
    /*@Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new BlockEntityChunkLoader();
    }
    *///?}
}
