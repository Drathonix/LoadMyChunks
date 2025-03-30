package com.drathonix.loadmychunks.common.bridge;

import com.drathonix.loadmychunks.common.system.ChunkDataModule;

public interface ILevelChunkMixin {
    //? if <=1.16.5 {
    /*void loadMyChunks$tick(ProfilerFiller profilerFiller);*/
    //?} else {
    void loadMyChunks$tick();
    //?}

    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
