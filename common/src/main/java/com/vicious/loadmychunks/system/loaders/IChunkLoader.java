package com.vicious.loadmychunks.system.loaders;

import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.system.ChunkLoaderManager;
import com.vicious.loadmychunks.system.control.LoadState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IChunkLoader {
    default LoadState getLoadState() {
        return LoadState.PERMANENT;
    }

    @NotNull CompoundTag save(@NotNull CompoundTag tag);
    void load(@NotNull CompoundTag tag);

    ResourceLocation getTypeId();
}