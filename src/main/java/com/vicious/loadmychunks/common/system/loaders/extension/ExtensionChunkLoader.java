package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IChunkPositioned;
import com.vicious.loadmychunks.common.system.loaders.PhantomChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.vicious.loadmychunks.common.util.ReferenceHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
        //? if >1.16.5
        hosts = ArrayUtils.removeAllOccurrences(hosts,ReferenceHelper.get(IChunkLoader.class,host));
        //? if <1.16.6
        /*hosts = ArrayUtils.removeAllOccurences(hosts, ReferenceHelper.get(IChunkLoader.class,host));*/
    }

    public boolean isUnhosted(){
        return hosts.length == 0;
    }

    @Override
    public void addHost(Object host){
        hosts = ArrayUtils.add(hosts, ReferenceHelper.get(IChunkLoader.class,host));
    }

    @SuppressWarnings("unchecked")
    public T getHost(int i) {
        return (T)hosts[i];
    }
}
