package com.vicious.loadmychunks.system.loaders;

import com.vicious.loadmychunks.system.control.LoadState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IChunkLoader {
    default LoadState getLoadState() {
        return LoadState.TICKING;
    }

    default void setLoadState(LoadState state){}

    @NotNull CompoundTag save(@NotNull CompoundTag tag);
    void load(@NotNull CompoundTag tag);

    ResourceLocation getTypeId();
}