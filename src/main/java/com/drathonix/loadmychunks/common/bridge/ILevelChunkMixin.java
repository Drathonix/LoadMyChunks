package com.drathonix.loadmychunks.common.bridge;

import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;

public interface ILevelChunkMixin {
    //? if <=1.16.5 {
    /*void loadMyChunks$tick(ProfilerFiller profilerFiller);
    *///?} else {
    void loadMyChunks$tick();
    //?}

    /**
     * Ticks entities. Critically important fact that this happens before block entity ticking.
     * @param profilerFiller
     */
    void loadMyChunks$tickEntities(ProfilerFiller profilerFiller);
    ChunkDataModule loadMyChunks$getDataModule();

    long loadMyChunks$posAsLong();
}
