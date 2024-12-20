package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.PhantomChunkLoader;
import net.minecraft.util.parsing.packrat.Atom;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicExtensionChunkLoader<T extends IChunkLoader> extends PhantomChunkLoader implements IExtensionChunkLoader<T> {
    protected AtomicReference<T>[] hosts;

    public AtomicExtensionChunkLoader() {
        super();
    }
    @SuppressWarnings("unchecked")
    public AtomicExtensionChunkLoader(AtomicReference<T> host, ChunkPos loadedChunk) {
        super(loadedChunk);
        hosts = new AtomicReference[]{host};
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeHost(Object loader){
        hosts = ArrayUtils.removeAllOccurrences(hosts,(AtomicReference<T>) loader);
    }

    public boolean isUnhosted(){
        return hosts.length == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addHost(Object host){
        hosts = ArrayUtils.add(hosts,(AtomicReference<T>) host);
    }

    public T getHost(int i) {
        return hosts[i].get();
    }
}
