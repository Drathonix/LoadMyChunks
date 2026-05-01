package com.drathonix.loadmychunks.common.registry;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

/**
 * Provides a way to iterate through all loaded chunks as defined by the implementer. This was added to allow support
 * for mods which have chunks which exist separately from the actual world or are not handled by the vanilla
 * updating chunk map.
 * @author Jack Andersen
 * @since 1.2.3
 */
public interface ILoadedChunkProvider {
    void iterateChunks(ServerLevel level, Consumer<ChunkHolder> consumer);
    String getName();

    default void iterateChunksBlockEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer){
        iterateChunks(level, value->{
            ServerChunkCache scc = level.getChunkSource();
            if (scc.chunkMap.getDistanceManager().inBlockTickingRange(value.getPos().toLong())) {
                consumer.accept(value);
            }
        });
    }

    default void iterateChunksEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer){
        iterateChunks(level, value->{
            ServerChunkCache scc = level.getChunkSource();
            if (((IChunkMapMixin) scc.chunkMap).lmc$inEntityTickingRange(value.getPos().toLong())) {
                consumer.accept(value);
            }
        });
    }
}
