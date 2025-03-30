package com.drathonix.loadmychunks.common.bridge;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.ChunkPos;

public interface IChunkMapMixin {
    Long2ObjectLinkedOpenHashMap<ChunkHolder> lmc$getUpdatingChunkMap();
    boolean lmc$playerDistCheck(ChunkPos pos);
}
