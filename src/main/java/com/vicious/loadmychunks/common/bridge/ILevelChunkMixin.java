package com.vicious.loadmychunks.common.bridge;

import com.vicious.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.util.profiling.ProfilerFiller;

public interface ILevelChunkMixin {
    //? if <=1.16.5
    void loadMyChunks$tick(ProfilerFiller profilerFiller);
    //? if >1.16.5
    /*void loadMyChunks$tick();*/

    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
