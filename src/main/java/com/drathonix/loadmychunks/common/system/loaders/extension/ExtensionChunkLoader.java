package com.drathonix.loadmychunks.common.system.loaders.extension;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.PhantomChunkLoader;
import com.drathonix.loadmychunks.common.util.ReferenceHelper;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public abstract class ExtensionChunkLoader<T extends IChunkLoader> extends PhantomChunkLoader implements IExtensionChunkLoader<T> {
    protected IChunkLoader[] hosts;

    public ExtensionChunkLoader() {
        super();
    }
    public ExtensionChunkLoader(IChunkLoader host, ChunkPos loadedChunk) {
        super(loadedChunk);
        hosts = new IChunkLoader[]{host};
    }

    @Override
    public void removeHost(Object host){
        //? if >1.16.5 {
        hosts = ArrayUtils.removeAllOccurrences(hosts,ReferenceHelper.get(IChunkLoader.class,host));
        //?} else {
        /*hosts = ArrayUtils.removeAllOccurences(hosts, ReferenceHelper.get(IChunkLoader.class,host));
        *///?}
    }

    @Override
    public int getNumberOfHosts() {
        return hosts.length;
    }

    @Override
    public void addHost(Object host){
        if(!ArrayUtils.contains(hosts,ReferenceHelper.get(IChunkLoader.class,host))) {
            hosts = ArrayUtils.add(hosts, ReferenceHelper.get(IChunkLoader.class, host));
        }
    }

    @SuppressWarnings("unchecked")
    public T getHost(int i) {
        if(hosts.length <= i || i < 0){
            return null;
        }
        return (T)hosts[i];
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(hosts);
    }
}
