package com.vicious.loadmychunks.common.block.blockentity;


import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.BlockLagometer;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityLagometer extends BEBase
    //? if <=1.16.5
        /*implements TickableBlockEntity*/
    {
    private ChunkDataModule cdm;

        //? if <=1.16.5 {
    /*public BlockEntityLagometer() {
        super(LMCContent.lagometerBlockEntity.get());
    }
    *///?}

    @Override
    public void validate(Level level) {
        super.validate(level);
        if(level instanceof ServerLevel) {
            cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) level, getBlockPos());
        }
    }

    //? if >1.16.5 {
    public BlockEntityLagometer(BlockPos blockPos, BlockState blockState) {
        super(LMCContent.lagometerBlockEntity.get(), blockPos, blockState);
    }
    //?}


    public void serverTick(BlockState blockState){
        if(cdm != null){
            cdm.timeRegardless=true;
            int prevLag = blockState.getValue(BlockLagometer.LAG);
            int currLag = (int) (cdm.getTickTimer().getLagFraction()*15);
            if(prevLag != currLag){
                this.level.setBlock(this.worldPosition, blockState.setValue(BlockLagometer.LAG, currLag),3);
            }
        }
    }

    //? if <=1.16.5 {
    /*@Override
    public void tick() {
        serverTick(getBlockState());
    }
    *///?}
}