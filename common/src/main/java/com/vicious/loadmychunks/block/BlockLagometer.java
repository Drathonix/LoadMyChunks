package com.vicious.loadmychunks.block;


import com.mojang.serialization.MapCodec;
import com.vicious.loadmychunks.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.registry.LMCContent;
import com.vicious.loadmychunks.system.ChunkDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLagometer extends BaseEntityBlock {
    public static final MapCodec<BlockLagometer> CODEC = simpleCodec(BlockLagometer::new);
    public static final IntegerProperty LAG = IntegerProperty.create("lmc_lag",0,15);


    public BlockLagometer(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(LAG, 0));
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

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if(level instanceof ServerLevel sl) {
            return (int) ChunkDataManager.getOrCreateChunkData(sl, blockPos).getTickTimer().getLagFraction()*15;
        }
        return 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(LAG);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if(blockEntityType == LMCContent.lagometerBlockEntity.get()){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if(blockEntity instanceof BlockEntityLagometer bel){
                    bel.serverTick(blockState1);
                }
            });
        }
        return null;
    }
}
