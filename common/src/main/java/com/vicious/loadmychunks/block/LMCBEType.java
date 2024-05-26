package com.vicious.loadmychunks.block;

import com.mojang.datafixers.types.Type;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class LMCBEType<T extends BlockEntity> extends BlockEntityType<T> {
    private final Factory<? extends T> supp;
    public LMCBEType(Factory<? extends T> blockEntitySupplier, Set<Block> set, Type<?> type) {
        super(null, set, type);
        this.supp =blockEntitySupplier;
    }

    @Nullable
    public T create() {
        return this.supp.create();
    }

    @Nullable
    public T getBlockEntity(BlockGetter blockGetter, BlockPos blockPos) {
        BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);
        return blockEntity != null && blockEntity.getType() == this ? (T) blockEntity : null;
    }

    @FunctionalInterface
    public interface Factory<T extends BlockEntity> {
        T create();
    }
}
