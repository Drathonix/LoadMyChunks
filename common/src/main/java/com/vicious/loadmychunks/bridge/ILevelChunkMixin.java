package com.vicious.loadmychunks.bridge;

import com.vicious.loadmychunks.system.ChunkDataModule;
import net.minecraft.util.profiling.ProfilerFiller;

public interface ILevelChunkMixin {
    void loadMyChunks$tick(ProfilerFiller profilerFiller);
    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
