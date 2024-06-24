package com.vicious.loadmychunks.common.system;


import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
//? if >1.18.2
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.apache.logging.log4j.core.jmx.Server;
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

    public static LevelChunkLoaderManager getManager(ServerLevel level){
        return levelManagers.computeIfAbsent(level, k->new LevelChunkLoaderManager(level));
    }

    //? if >1.16.5 {
    public static LevelChunkLoaderManager loadManager(ServerLevel level, CompoundTag tag){
        if(levelManagers.containsKey(level)){
            levelManagers.get(level).clear();
            LevelChunkLoaderManager out = new LevelChunkLoaderManager(level,tag);
            levelManagers.replace(level,out);
            return out;
        }
        else{
            return levelManagers.put(level, new LevelChunkLoaderManager(level,tag));
        }
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
    public static int getCountChunkLoadersOf(@Nullable UUID owner) {
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
    public static int getCountLoadedChunksOf(@Nullable UUID owner) {
        int count = 0;
        if(owner == null){
            return 0;
        }
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            l1:
            for (ChunkDataModule dataModule : value.getChunkDataModules()) {
                for (IChunkLoader loader : dataModule.getLoaders()) {
                    if(loader instanceof IOwnable && owner.equals(((IOwnable)loader).getOwner())){
                        count++;
                        continue l1;
                    }
                }
            }
        }
        return count;
    }

    public static void markShutDown(ServerLevel level, ChunkPos chunkPos) {
        getManager(level).shutDown(chunkPos);
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

    public static ChunkDataModule getOrCreateChunkData(ServerLevel level, BlockPos pos) {
        return getOrCreateChunkData(level,new ChunkPos(pos));
    }

    public static ChunkDataModule getOrCreateChunkData(ServerLevel level, ChunkPos pos) {
        return getManager(level).getOrCreateData(pos);
    }
    public static ChunkDataModule getOrCreateChunkData(ServerLevel level, long pos) {
        return getManager(level).getOrCreateData(pos);
    }

    public static <T extends IChunkLoader> T computeChunkLoaderIfAbsent(ServerLevel sl, BlockPos blockPos, Class<T> type, Predicate<T> predicate, Supplier<T> supplier) {
        return getManager(sl).computeChunkLoaderIfAbsent(blockPos,type,predicate,supplier);
    }

    public static void clear() {
        for (LevelChunkLoaderManager value : levelManagers.values()) {
            value.clear();
        }
        levelManagers.clear();
    }

    public static class LevelChunkLoaderManager extends SavedData{
        private final Long2ObjectLinkedOpenHashMap<ChunkDataModule> data = new Long2ObjectLinkedOpenHashMap<>();
        private final Set<ChunkDataModule> shutoffLoaders = new HashSet<>();
        private final ServerLevel level;

        public LevelChunkLoaderManager(@NotNull ServerLevel level){
            //? if <=1.16.5
            /*super("loadmychunks_manager");*/
            this.level=level;
            level.getServer().addTickable(this::tick);
        }

        //? if >1.16.5 {
        public LevelChunkLoaderManager(@NotNull ServerLevel level, @NotNull CompoundTag tag){
            this(level);
            for (String key : tag.getAllKeys()) {
                long index = Long.parseLong(key);
                ChunkPos pos = new ChunkPos(index);
                ChunkDataModule module = getOrCreateData(index);
                module.load(tag.getCompound(key));
                module.update();
                if(module.onCooldown()){
                    shutDown(pos);
                }
                else{
                    module.getLoadState().apply(level,pos);
                }
            }
        }
        //?}

        public @NotNull ChunkDataModule getOrCreateData(@NotNull ChunkPos pos){
            return getOrCreateData(pos.toLong());
        }

        public void addChunkLoader(IChunkLoader loader, ChunkPos pos){
            addChunkLoader(loader,pos.toLong());
        }

        public void addChunkLoader(IChunkLoader loader, long pos){
            ChunkDataModule cdm = getOrCreateData(pos);
            if(cdm.addLoader(loader)) {
                if(cdm.getLoadState().shouldLoad()){
                    cdm.startGrace();
                }
                cdm.getLoadState().apply(level, pos);
            }
            setDirty();
        }

        public void removeChunkLoader(IChunkLoader loader, ChunkPos pos){
            removeChunkLoader(loader,pos.toLong());
        }

        public void removeChunkLoader(IChunkLoader loader, long pos){
            ChunkDataModule cdm = getOrCreateData(pos);
            if(cdm.removeLoader(loader)) {
                cdm.getLoadState().apply(level, pos);
            }
            setDirty();
        }

        public @NotNull ChunkDataModule getOrCreateData(long pos){
            ChunkDataModule cdm = data.computeIfAbsent(pos, ChunkDataModule::new);
            setDirty();
            return cdm;
        }
        //? if <=1.16.5 {
        /*@Override
        public void load(CompoundTag tag) {
            for (String key : tag.getAllKeys()) {
                long index = Long.parseLong(key);
                ChunkPos pos = new ChunkPos(index);
                ChunkDataModule module = getOrCreateData(index);
                module.load(tag.getCompound(key));
                module.update();
                if(module.onCooldown()){
                    shutDown(pos);
                }
                else{
                    module.getLoadState().apply(level,pos);
                }
            }
        }
        *///?}

        //? if <=1.20.5
        /*@Override*/
        public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
            data.forEach((k,v)->{
                if(!v.shouldPersist()) {
                    compoundTag.put(String.valueOf(k), v.save());
                }
            });
            return compoundTag;
        }

        public void tick(){
            Iterator<ChunkDataModule> iterator = shutoffLoaders.iterator();
            while (iterator.hasNext()){
                ChunkDataModule module = iterator.next();
                if(!module.onCooldown()) {
                    iterator.remove();
                    module.update();
                    if(module.getLoadState().shouldLoad()){
                        module.startGrace();
                    }
                    module.getLoadState().apply(level, module.getPosition());
                }

            }
            setDirty();
        }

        public void shutDown(@NotNull ChunkPos chunkPos) {
            ChunkDataModule module = data.get(chunkPos.toLong());
            shutoffLoaders.add(module);
            module.getLoadState().apply(level,chunkPos);
            setDirty();
        }

        public Collection<ChunkDataModule> getChunkDataModules() {
            return data.values();
        }

        public String getLevelName() {
            return ((ServerLevelData)level.getLevelData()).getLevelName();
        }

        @SuppressWarnings("all")
        public <T extends IChunkLoader> T computeChunkLoaderIfAbsent(BlockPos blockPos, Class<T> type, Predicate<T> predicate, Supplier<T> supplier) {
            ChunkDataModule cdm = getOrCreateData(new ChunkPos(blockPos));
            for (IChunkLoader loader : cdm.getLoaders()) {
                if(loader.getClass() == type){
                    if(predicate.test((T) loader)){
                        return (T) loader;
                    }
                }
            }
            T out = supplier.get();
            addChunkLoader(out,new ChunkPos(blockPos));
            return out;
        }

        public void clear() {
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
