package com.vicious.loadmychunks.common.block.blockentity;


import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.BlockLagometer;
import com.vicious.loadmychunks.common.bridge.IDestroyable;
import com.vicious.loadmychunks.common.bridge.IInformable;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityLagometer extends BEBase implements IInformable {
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
            cdm.addRecipient(this);
        }
    }

    //? if >1.16.5 {
    public BlockEntityLagometer(BlockPos blockPos, BlockState blockState) {
        super(LMCContent.lagometerBlockEntity.get(), blockPos, blockState);
    }
    //?}

    @Override
    public void informLagFrac(float frac) {
        if(!isRemoved()) {
            BlockState blockState = getBlockState();
            int prevLag = blockState.getValue(BlockLagometer.LAG);
            int currLag = (int) (cdm.getTickTimer().getLagFraction() * 15);
            if (prevLag != currLag) {
                this.level.setBlock(this.worldPosition, blockState.setValue(BlockLagometer.LAG, currLag), 3);
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(cdm != null){
            cdm.removeRecipient(this);
        }
    }
}