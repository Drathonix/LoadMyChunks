package com.vicious.loadmychunks.common.block.blockentity;

import net.minecraft.core.BlockPos;
//? if >1.20.5
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BEBase extends BlockEntity {
    //? if >1.16.5 {
    public BEBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    //?}
    //? if <=1.16.5 {
    /*public BEBase(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }
    *///?}

    //? if <=1.16.5 {
    /*@Override
    public void setLevelAndPosition(Level level, BlockPos blockPos) {
        super.setLevelAndPosition(level, blockPos);
        validate(level);
    }
    *///?}

    //? if >1.16.5 {
    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        validate(level);
    }
    //?}


    //? if >1.20.5 {
    @Override
    protected void loadAdditional(@NotNull CompoundTag arg, HolderLookup.@NotNull Provider arg2) {
        this.write(arg);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag arg, HolderLookup.@NotNull Provider arg2) {
        this.read(arg);
    }
    //?}

    protected void write(CompoundTag tag) {}

    protected void read(CompoundTag tag) {}

    //? if >1.16.5 && <1.20.5 {
    /*@Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.write(compoundTag);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        this.read(compoundTag);
    }
    *///?}

    //? if <=1.16.5 {
    /*@Override
    public CompoundTag save(CompoundTag compoundTag) {
        super.save(compoundTag);
        this.read(compoundTag);
        return compoundTag;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compoundTag) {
        super.load(blockState, compoundTag);
        this.write(compoundTag);
    }
    *///?}

    public void validate(Level level){

    }
}
