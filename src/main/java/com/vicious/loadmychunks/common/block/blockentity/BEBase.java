package com.vicious.loadmychunks.common.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BEBase extends BlockEntity {
    //? if <=1.16.5 {
    public BEBase(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }
    //?}

    //? if <=1.16.5 {
    @Override
    public void setLevelAndPosition(Level level, BlockPos blockPos) {
        super.setLevelAndPosition(level, blockPos);
        validate(level);
    }
    //?}

    //? if >1.16.5 {
    /*@Override
    public void setLevel(Level level) {
        super.setLevel(level)
        validate(level)
    }
    *///?}


    public void validate(Level level){

    }
}
