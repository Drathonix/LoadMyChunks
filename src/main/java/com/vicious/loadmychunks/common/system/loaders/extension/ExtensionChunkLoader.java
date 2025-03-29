package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.control.ILoadState;
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
        //? if >1.16.5 {
        hosts = ArrayUtils.removeAllOccurrences(hosts,ReferenceHelper.get(IChunkLoader.class,host));
        //?} else {
        /*hosts = ArrayUtils.removeAllOccurences(hosts, ReferenceHelper.get(IChunkLoader.class,host));*/
        //?}
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

    @Override
    public ILoadState getActiveState() {
        if(!isUnhosted()){
            ILoadState state = LoadStateRegistry.DISABLED;
            for (IChunkLoader host : hosts) {
                state = host.getActiveState().getSuperiorLoadState(state);
            }
            return state;
        }
        else{
            return LoadStateRegistry.DISABLED;
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
