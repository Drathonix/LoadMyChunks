package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public interface IExtensionChunkLoader<T extends IChunkLoader> extends IChunkLoader {
    default @Nullable IChunkLoader getPrimaryHostLoader(){
        return getHost(0);
    }

    default int getExtensionDistance() {
        IChunkLoader loader = getPrimaryHostLoader();
        if(loader != null){
            ChunkPos host = loader.getChunkPos();
            ChunkPos extension = getChunkPos();
            return Math.max(Math.abs(host.x-extension.x), Math.abs(host.z-extension.z));
        }
        return -1;
    }

    /**
     * Accepts only AtomicReference of IChunkLoader and IChunkLoaders
     */
    void removeHost(Object host);

    boolean isUnhosted();

    /**
     * Accepts only AtomicReference of IChunkLoader and IChunkLoaders
     */
    void addHost(Object host);

    T getHost(int i);

    default void removeHostAndUnload(ServerLevel level, Object host) {
        removeHost(host);
        if(isUnhosted()){
            ChunkDataManager.removeChunkLoader(level, this.getChunkPos(), this);
        }
    }
}
