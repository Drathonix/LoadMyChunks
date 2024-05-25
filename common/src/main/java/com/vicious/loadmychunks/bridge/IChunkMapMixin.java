package com.vicious.loadmychunks.bridge;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;

public interface IChunkMapMixin {
    Long2ObjectLinkedOpenHashMap<ChunkHolder> loadMyChunks$getUpdatingChunkMap();
}
