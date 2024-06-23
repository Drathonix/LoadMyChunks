package com.vicious.loadmychunks.common.block;


import com.mojang.serialization.MapCodec;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLagometer extends BaseEntityBlock {
    //? if >=1.20.4 {
    /*public static final MapCodec<BlockLagometer> CODEC = simpleCodec(BlockLagometer::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    *///?}
    public static final IntegerProperty LAG = IntegerProperty.create("lmc_lag",0,15);


    public BlockLagometer(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(LAG, 0));
    }



    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    //? if <=1.16.5 {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new BlockEntityLagometer();
    }
    //?}

    //? if >1.16.5 {
    /*@Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityLagometer(blockPos,blockState);
    }
    *///?}


    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if(level instanceof ServerLevel) {
            return (int) (ChunkDataManager.getOrCreateChunkData((ServerLevel)level, blockPos).getTickTimer().getLagFraction()*15.0);
        }
        return 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(LAG);
    }


    //? if >1.16.5 {
    /*@Nullable
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
    *///?}
}