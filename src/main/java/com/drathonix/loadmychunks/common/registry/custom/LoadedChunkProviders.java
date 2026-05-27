package com.drathonix.loadmychunks.common.registry.custom;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.registry.ILoadedChunkProvider;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Registration location for all loaded chunk providers.
 * @author Jack Andersen
 * @since 1.2.3
 */
public class LoadedChunkProviders {
    private static final List<ILoadedChunkProvider> loadedChunkProviders = new ArrayList<>();

    static {
        // The vanilla chunk provider.
        addLoadedChunkProvider(new ILoadedChunkProvider() {
            @Override
            public void iterateChunks(ServerLevel level, Consumer<ChunkHolder> consumer) {
                ServerChunkCache scc = level.getChunkSource();
                Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = ((IChunkMapMixin) scc.chunkMap).lmc$getUpdatingChunkMap();
                for (ChunkHolder value : updatingChunkMap.values()) {
                    // somehow this can happen? The UCM shouldn't have null values in it but I guess other mods can mess with that.
                    if(value != null) {
                        consumer.accept(value);
                    }
                }
            }

            @Override
            public String getName() {
                return "loadmychunks:vanilla_chunk_map";
            }
        });
    }

    public static void addLoadedChunkProvider(ILoadedChunkProvider loadedChunkProvider) {
        loadedChunkProviders.add(loadedChunkProvider);
    }

    public static synchronized void iterateChunks(ServerLevel level, Consumer<ChunkHolder> consumer){
        for (ILoadedChunkProvider loadedChunkProvider : loadedChunkProviders) {
            loadedChunkProvider.iterateChunks(level, consumer);
        }
    }

    public static synchronized void iterateChunksEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer){
        for (ILoadedChunkProvider loadedChunkProvider : loadedChunkProviders) {
            loadedChunkProvider.iterateChunksEntityTicking(level, consumer);
        }
    }

    public static synchronized void iterateChunksBlockEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer){
        for (ILoadedChunkProvider loadedChunkProvider : loadedChunkProviders) {
            loadedChunkProvider.iterateChunksBlockEntityTicking(level, consumer);
        }
    }
}
