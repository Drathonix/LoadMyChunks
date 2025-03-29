//? if cc-tweaked {
package com.vicious.loadmychunks.common.integ.cct.peripheral;

import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ChunkLoaderPeripheral extends AbstractChunkLoaderPeripheral {
    private final BlockPos pos;
    private final IChunkLoader loader;
    private final ServerLevel level;
    private final ChunkDataModule cdm;

    public ChunkLoaderPeripheral(BlockPos pos, Level level, IChunkLoader loader){
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
    public String getType() {
        return "lmc_chunk_loader";
    }

    @Override
    protected @NotNull ServerLevel getLevel() {
        return level;
    }

    @Override
    protected @NotNull BlockPos getPosition() {
        return pos;
    }

    private static final Set<String> additional = new HashSet<>();
    static {
        additional.add("lmc_lagometer");
    }

    @Override
    public Set<String> getAdditionalTypes() {
        return additional;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }
}
//?}