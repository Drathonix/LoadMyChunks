package com.vicious.loadmychunks.bridge;

import com.vicious.loadmychunks.system.ChunkDataModule;

public interface ILevelChunkMixin {
    void loadMyChunks$tick();
    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
