package com.vicious.loadmychunks.block.blockentity;

import com.vicious.loadmychunks.block.BlockLagometer;
import com.vicious.loadmychunks.registry.LMCContent;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityLagometer extends BlockEntity implements IInformable {
    private ChunkDataModule cdm;
    public BlockEntityLagometer(BlockPos blockPos, BlockState blockState) {
        this(LMCContent.lagometerBlockEntity.get(), blockPos, blockState);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if(level instanceof ServerLevel sl) {
            cdm = ChunkDataManager.getOrCreateChunkData(sl, getBlockPos());
        }
    }

    public BlockEntityLagometer(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void serverTick(BlockState blockState){
        if(cdm != null){
            int prevLag = blockState.getValue(BlockLagometer.LAG);
            int currLag = (int) (cdm.getTickTimer().getLagFraction()*15);
            if(prevLag != currLag){
                blockState.setValue(BlockLagometer.LAG,currLag);
            }
        }
    }
}
