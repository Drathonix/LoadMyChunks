package com.drathonix.loadmychunks.common.system.loaders.extension;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.util.ReferenceHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * Stores extension chunk loaders based on their grid position relative to the host.
 * This does not use the world chunk pos.
 */
public class ExtensionChunkLoaders extends Long2ObjectOpenHashMap<IExtensionChunkLoader<?>> {
    private final ServerLevel level;
    private final Object host;

    public ExtensionChunkLoaders(ServerLevel level, Object host) {
        ReferenceHelper.assertIsType(IChunkLoader.class,host);
        this.level = level;
        this.host = host;
    }

    //TODO: improve move efficiency.
    public synchronized <T extends IExtensionChunkLoader<?>> void recompute(Class<T> extensionType, int range, Factory<T> factory){
        /*if(range <= 0){
            for(IExtensionChunkLoader<?> extensionChunkLoader : this.values()){
                extensionChunkLoader.removeHostAndUnload(level,host);
            }
            this.clear();
            return;
        }
        for (IExtensionChunkLoader<?> extensionChunkLoader : new ArrayList<>(values)) {
            if(extensionChunkLoader.getExtensionDistance() > range){
                remove(extensionChunkLoader.getChunkPos().toLong());
            }
        }*/
        for(IExtensionChunkLoader<?> extensionChunkLoader : this.values()){
            extensionChunkLoader.removeHostAndUnload(level,host);
        }
        this.clear();
        if(range < 1){
            return;
        }
        ChunkPos hostPos = ReferenceHelper.get(IChunkLoader.class,host).getChunkPos();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if(z == 0 && x == 0){
                    continue;
                }
                ChunkPos pos = new ChunkPos(x, z);
                long gridPos = pos.toLong();
                //if(!containsKey(gridPos)){
                    ChunkPos cp = new ChunkPos(hostPos.x+x,hostPos.z+z);
                    T loader = ChunkDataManager.computeChunkLoaderIfAbsent(level,cp,extensionType,present->true,()->factory.create(cp));
                    loader.addHost(this.host);
                    put(gridPos,loader);
               // }
            }
        }
    }

    @Override
    public synchronized IExtensionChunkLoader<?> remove(long k) {
        IExtensionChunkLoader<?> ecl = super.remove(k);
        if(ecl != null){
            ecl.removeHostAndUnload(level,host);
        }
        return ecl;
    }

    @FunctionalInterface
    public interface Factory<T extends IExtensionChunkLoader<?>> {
        T create(ChunkPos position);
    }
}
