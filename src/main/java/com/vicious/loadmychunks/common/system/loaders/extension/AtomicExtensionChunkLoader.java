package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.PhantomChunkLoader;
import com.vicious.loadmychunks.common.util.ReferenceHelper;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.ArrayUtils;

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
        //? if >1.16.5
        hosts = ArrayUtils.removeAllOccurrences(hosts,(AtomicReference<T>) loader);
        //? if <1.16.6
        /*hosts = ArrayUtils.removeAllOccurences(hosts,(AtomicReference<T>) loader);*/
    }

    @Override
    public int getNumberOfHosts() {
        return hosts.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addHost(Object host){
        if(!ArrayUtils.contains(hosts,host)) {
            hosts = ArrayUtils.add(hosts,(AtomicReference<T>) host);
        }
    }

    public T getHost(int i) {
        if(hosts.length <= i || i < 0){
            return null;
        }
        return hosts[i].get();
    }
}
