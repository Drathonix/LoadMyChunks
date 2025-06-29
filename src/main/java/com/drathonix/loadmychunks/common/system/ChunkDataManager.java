package com.drathonix.loadmychunks.common.system;


import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.IOwnable;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
//? if >1.18.2
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Maintains the records of all chunk loaders and chunk load states handled by LoadMyChunks.
 */
public class ChunkDataManager {
    private static final Map<ServerLevel,LevelChunkLoaderManager> levelManagers = new IdentityHashMap<>();

    public static synchronized boolean hasExceededOwnershipCap(UUID uuid) {
        return hasExceededOwnershipCap(uuid,0);
    }

    public static synchronized boolean hasExceededOwnershipCap(UUID uuid, int added){
        if(uuid == null) uuid = Util.NIL_UUID;
        if(LMCConfig.limitSettings.enabledForEnvironment && uuid == Util.NIL_UUID){
            return getCountLoadedChunksOf(uuid)+added > LMCConfig.limitSettings.limit;
        }
        else if(LMCConfig.limitSettings.enabledForPlayers){
            return getCountLoadedChunksOf(uuid)+added > LMCConfig.limitSettings.limit;
        }
        return false;
    }

    public synchronized static void markChunkOwnedBy(ServerLevel level, long longChunkPos, @Nullable UUID uuid){
        getManager(level).markChunkOwnedBy(longChunkPos,uuid);
        if(hasExceededOwnershipCap(uuid)){
            updateCDMSofUUID(uuid);
        }
    }
    public synchronized static void markChunkNotOwnedBy(ServerLevel level, long longChunkPos, @Nullable UUID uuid){
        getManager(level).markChunkNotOwnedBy(longChunkPos,uuid);
        if(!hasExceededOwnershipCap(uuid)){
            updateCDMSofUUID(uuid);
        }
    }

    public static void updateCDMSofUUID(UUID uuid){
        for (ServerLevel serverLevel : levelManagers.keySet()) {
            LevelChunkLoaderManager value = levelManagers.get(serverLevel);
            for (long l : value.forcedChunksByUUID.getOrDefault(uuid, new LongOpenHashSet())) {
                ChunkDataModule cdm = value.getOrCreateData(l);
                ILoadState loadState = cdm.getLoadState();
                cdm.update();
                cdm.updateChunkLoadState(serverLevel,loadState);
            }
        }
    }

    public static synchronized LevelChunkLoaderManager getManager(ServerLevel level){
        return levelManagers.computeIfAbsent(level, k->new LevelChunkLoaderManager(level));
    }

    //? if >1.16.5 {
    public static synchronized LevelChunkLoaderManager loadManager(ServerLevel level, CompoundTag tag){
        LevelChunkLoaderManager manager = getManager(level);
        manager.load(tag);
        return manager;
    }
    //?}

    public static @NotNull Map<String,List<IChunkLoader>> getChunkLoadersOf(@Nullable UUID owner) {
        if(owner == null){
            return new HashMap<>();
        }
        Map<String,List<IChunkLoader>> results = new HashMap<>();
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            List<IChunkLoader> loaders = results.computeIfAbsent(value.getLevelName(),k->new ArrayList<>());
            for (ChunkDataModule dataModule : value.getChunkDataModules()) {
                for (IChunkLoader loader : dataModule.getLoaders()) {
                    if(loader instanceof IOwnable && owner.equals(((IOwnable)loader).getOwner())){
                        loaders.add(loader);
                    }
                }
            }
        }
        return results;
    }
    public static int getCountChunkLoadersOf(@NotNull UUID owner) {
        if(owner == null){
            return 0;
        }
        int count = 0;
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            for (ChunkDataModule dataModule : value.getChunkDataModules()) {
                for (IChunkLoader loader : dataModule.getLoaders()) {
                    if(loader instanceof IOwnable && owner.equals(((IOwnable)loader).getOwner())){
                        count++;
                    }
                }
            }
        }
        return count;
    }
    public synchronized static int getCountLoadedChunksOf(@NotNull UUID owner) {
        int count = 0;
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            count += value.getCountLoadedChunksOf(owner);
        }
        return count;
    }

    public static void markShutDown(ServerLevel level, ChunkPos chunkPos, ILoadState previous) {
        getManager(level).shutDown(chunkPos,previous);
    }

    public static void removeChunkLoader(ServerLevel level, BlockPos pos, IChunkLoader loader){
        removeChunkLoader(level,new ChunkPos(pos),loader);
    }

    public static void removeChunkLoader(ServerLevel level, ChunkPos pos, IChunkLoader loader){
        getManager(level).removeChunkLoader(loader,pos);
    }

    public static void removeChunkLoader(ServerLevel level, long pos, IChunkLoader loader) {
        getManager(level).removeChunkLoader(loader, pos);
    }

    public static void addChunkLoader(ServerLevel level, BlockPos pos, IChunkLoader loader){
        addChunkLoader(level,new ChunkPos(pos),loader);
    }

    public static void addChunkLoader(ServerLevel level, ChunkPos pos, IChunkLoader loader){
        getManager(level).addChunkLoader(loader,pos);
    }

    public static void addChunkLoader(ServerLevel level, long pos, IChunkLoader loader) {
        getManager(level).addChunkLoader(loader, pos);
    }

    public static @NotNull ChunkDataModule getOrCreateChunkData(ServerLevel level, BlockPos pos) {
        return getOrCreateChunkData(level,new ChunkPos(pos));
    }

    public static @NotNull ChunkDataModule getOrCreateChunkData(ServerLevel level, ChunkPos pos) {
        return getManager(level).getOrCreateData(pos);
    }
    public static @NotNull ChunkDataModule getOrCreateChunkData(ServerLevel level, long pos) {
        return getManager(level).getOrCreateData(pos);
    }

    public static <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ServerLevel sl, BlockPos blockPos, Class<T> type, boolean doAdd, Predicate<T> predicate, Supplier<T> supplier) {
        return getManager(sl).computeChunkLoaderIfAbsent(blockPos,type,doAdd,predicate,supplier);
    }
    public static <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ServerLevel sl, ChunkPos chunkPos, Class<T> type, boolean doAdd, Predicate<T> predicate, Supplier<T> supplier) {
        return getManager(sl).computeChunkLoaderIfAbsent(chunkPos,type,doAdd,predicate,supplier);
    }

    public static <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ServerLevel sl, BlockPos blockPos, Class<T> type, Predicate<T> predicate, Supplier<T> supplier) {
        return getManager(sl).computeChunkLoaderIfAbsent(blockPos,type,true,predicate,supplier);
    }
    public static <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ServerLevel sl, ChunkPos chunkPos, Class<T> type, Predicate<T> predicate, Supplier<T> supplier) {
        return getManager(sl).computeChunkLoaderIfAbsent(chunkPos,type,true,predicate,supplier);
    }

    public static void clear() {
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            value.clear();
        }
        levelManagers.clear();
    }

    public static void setDirty(ServerLevel level){
        getManager(level).setDirty();
    }

    public static boolean isForced(ServerLevel level,ChunkPos pos) {
        return getOrCreateChunkData(level,pos).getLoadState().shouldLoad();
    }

    public synchronized static void handleConfigReload() {
        for (ServerLevel level : levelManagers.keySet()) {
            LevelChunkLoaderManager value = levelManagers.get(level);
            value.configReloaded=true;
        }
    }

    public synchronized static void requestUpdate(ServerLevel level, ChunkPos chunkPos) {
        getManager(level).requestUpdate(chunkPos);
    }

    public static class LevelChunkLoaderManager extends SavedData{
        private final Long2ObjectLinkedOpenHashMap<ChunkDataModule> data = new Long2ObjectLinkedOpenHashMap<>();
        private final Set<ChunkDataModule> shutoffLoaders = new HashSet<>();
        private final Map<UUID, LongOpenHashSet> forcedChunksByUUID = new HashMap<>();
        private final ServerLevel level;
        protected boolean configReloaded = false;

        public synchronized void markChunkOwnedBy(long longChunkPos, @Nullable UUID uuid){
            if(uuid == null) uuid = Util.NIL_UUID;
            forcedChunksByUUID.computeIfAbsent(uuid, k -> new LongOpenHashSet()).add(longChunkPos);
        }
        public synchronized void markChunkNotOwnedBy(long longChunkPos, @Nullable UUID uuid){
            if(uuid == null) uuid = Util.NIL_UUID;
            if(forcedChunksByUUID.containsKey(uuid)){
                LongOpenHashSet set = forcedChunksByUUID.get(uuid);
                set.remove(longChunkPos);
                if(set.isEmpty()){
                    forcedChunksByUUID.remove(uuid);
                }
            }
        }

        public synchronized int getCountLoadedChunksOf(@NotNull UUID owner) {
            return forcedChunksByUUID.getOrDefault(owner,new LongOpenHashSet()).size();
        }

        public LevelChunkLoaderManager(@NotNull ServerLevel level){
            //? if <=1.16.5
            /*super("loadmychunks_manager");*/
            this.level=level;
            level.getServer().addTickable(this::tick);
        }

        public @NotNull ChunkDataModule getOrCreateData(@NotNull ChunkPos pos){
            return getOrCreateData(pos.toLong());
        }

        public void addChunkLoader(IChunkLoader loader, ChunkPos pos){
            addChunkLoader(loader,pos.toLong());
        }

        public synchronized void addChunkLoader(IChunkLoader loader, long pos){
            ChunkDataModule cdm = getOrCreateData(pos);
            if(loader instanceof IOwnable){
                markChunkOwnedBy(pos, ((IOwnable) loader).getOwner());
            }
            cdm.consumeLoadState(previous->{
                if(cdm.addLoader(level,loader)) {
                    cdm.updateChunkLoadState(level,previous);
                }
                setDirty();
            });
        }

        public void removeChunkLoader(IChunkLoader loader, ChunkPos pos){
            removeChunkLoader(loader,pos.toLong());
        }

        public synchronized void removeChunkLoader(IChunkLoader loader, long pos){
            ChunkDataModule cdm = getOrCreateData(pos);
            cdm.consumeLoadState(previous-> {
                if(cdm.removeLoader(level,loader)) {
                    cdm.updateChunkLoadState(level,previous);
                }
                setDirty();
            });
        }

        public synchronized @NotNull ChunkDataModule getOrCreateData(long pos){
            ChunkDataModule cdm = data.computeIfAbsent(pos, ChunkDataModule::new);
            setDirty();
            return cdm;
        }

        public void load(CompoundTag tag) {
            for (String key : tag.getAllKeys()) {
                long index = Long.parseLong(key);
                ChunkPos pos = new ChunkPos(index);
                ChunkDataModule module = getOrCreateData(index);
                module.consumeLoadState(previous->{
                    module.load(tag.getCompound(key),level);
                    module.update();
                    if(module.onCooldown()){
                        shutDown(pos,previous);
                    }
                    else{
                        module.getLoadState().apply(level,pos,previous);
                    }
                });
            }
        }

        //? if <=1.20.5
        /*@Override*/
        public synchronized @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
            data.forEach((k,v)->{
                if(v.shouldPersist()) {
                    compoundTag.put(String.valueOf(k), v.save());
                }
            });
            return compoundTag;
        }

        private int tickCounter = 0;
        private static final int purgeTimer = 20*100;

        public synchronized void tick(){
            if(configReloaded){
                for (ChunkDataModule cdm : getChunkDataModules()) {
                    cdm.consumeLoadState(previous->{
                        cdm.update();
                        cdm.updateChunkLoadState(level,previous);
                    });
                }
                configReloaded=false;
            }
            if(tickCounter >= purgeTimer){
                data.values().removeIf(module -> !module.shouldPersist() && !level.hasChunk(module.getPosition().x, module.getPosition().z));
                tickCounter = 0;
            }

            Iterator<ChunkDataModule> iterator = shutoffLoaders.iterator();
            while (iterator.hasNext()){
                ChunkDataModule cdm = iterator.next();
                if(!cdm.onCooldown()) {
                    iterator.remove();
                    cdm.consumeLoadState(previous->{
                        cdm.update();
                        if(cdm.getLoadState().shouldLoad()){
                            cdm.startGrace();
                        }
                        cdm.getLoadState().apply(level, cdm.getPosition(),previous);
                    });
                }

            }
            setDirty();
            tickCounter++;
        }

        public synchronized void shutDown(@NotNull ChunkPos chunkPos, @NotNull ILoadState previous) {
            ChunkDataModule module = data.get(chunkPos.toLong());
            shutoffLoaders.add(module);
            module.getLoadState().apply(level,chunkPos,previous);
            setDirty();
        }

        public Collection<ChunkDataModule> getChunkDataModules() {
            return data.values();
        }

        public String getLevelName() {
            return ((ServerLevelData)level.getLevelData()).getLevelName();
        }
        public synchronized  <T extends IChunkLoader> T computeChunkLoaderIfAbsent(BlockPos blockPos, Class<T> type, boolean doAdd, Predicate<T> predicate, Supplier<T> supplier) {
            return computeChunkLoaderIfAbsent(new ChunkPos(blockPos),type,doAdd,predicate,supplier);
        }
        @SuppressWarnings("all")
        public synchronized  <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ChunkPos pos, Class<T> type, boolean doAdd, Predicate<T> predicate, Supplier<T> supplier) {
            ChunkDataModule cdm = getOrCreateData(pos);
            for (IChunkLoader loader : cdm.getLoaders()) {
                if(loader.getClass() == type){
                    if(predicate.test((T) loader)){
                        return (T) loader;
                    }
                }
            }
            T out = supplier.get();
            if(doAdd) {
                addChunkLoader(out, pos);
            }
            return out;
        }

        public void requestUpdate(ChunkPos chunkPos) {
            ChunkDataModule cdm = getOrCreateData(chunkPos);
            cdm.consumeLoadState(previous -> {
                cdm.update(()->{
                    cdm.updateChunkLoadState(level,previous);
                });
            });
        }

        public synchronized void clear() {
            data.clear();
        }

        //? if >1.20.5 {
        @Override
        public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
            return save(compoundTag);
        }
        //?}
    }
}
