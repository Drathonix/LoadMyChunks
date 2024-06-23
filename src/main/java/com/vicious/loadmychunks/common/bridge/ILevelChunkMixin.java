package com.vicious.loadmychunks.common.bridge;

import com.vicious.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.util.profiling.ProfilerFiller;

public interface ILevelChunkMixin {
    void loadMyChunks$tick(ProfilerFiller profilerFiller);
    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
