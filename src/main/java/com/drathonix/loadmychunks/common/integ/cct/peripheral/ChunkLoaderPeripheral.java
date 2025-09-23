//? if cc-tweaked {
/*package com.drathonix.loadmychunks.common.integ.cct.peripheral;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChunkLoaderPeripheral extends AbstractChunkLoaderPeripheral {
    private final @NotNull BlockPos pos;
    private final @NotNull IChunkLoader loader;
    private final @NotNull ServerLevel level;
    private final @NotNull ChunkDataModule cdm;

    public ChunkLoaderPeripheral(@NotNull BlockPos pos, @NotNull Level level, @NotNull IChunkLoader loader){
        this.pos = pos;
        this.loader = loader;
        this.level=(ServerLevel)level;
        this.cdm = ChunkDataManager.getOrCreateChunkData(this.level,pos);
    }

    @Override
    public @NotNull IChunkLoader getChunkLoader() {
        return loader;
    }

    @Override
    public @NotNull ChunkDataModule getChunkDataModule() {
        return this.cdm;
    }

    @Override
    protected @NotNull ServerLevel getLevel() {
        return level;
    }

    @Override
    protected @NotNull BlockPos getPosition() {
        return pos;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }
}
*///?}