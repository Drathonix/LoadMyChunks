package com.vicious.loadmychunks.debug;


import com.mojang.serialization.MapCodec;
import com.vicious.loadmychunks.block.BlockChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

class DebugBlockLagger extends BaseEntityBlock {
    public DebugBlockLagger(Properties properties) {
        super(properties);
    }

    public static final MapCodec<DebugBlockLagger> CODEC = simpleCodec(DebugBlockLagger::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public DebugBlockEntityLagger newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DebugBlockEntityLagger(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return createTickerHelper(blockEntityType, DebugLoadMyChunks.laggerBlockEntity.get(), (level1, blockPos, blockState1, blockEntity) -> {
                try {
                    blockEntity.serverTick();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        return null;
    }

    /*@Override
    public void destroy(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        super.destroy(levelAccessor, blockPos, blockState);
        if(level instanceof ServerLevel sl) {
            ChunkLoaderManager.removeChunkLoader(sl, chunkLoader, getBlockPos());
        }
    }*/
}
