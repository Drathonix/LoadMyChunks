package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.bridge.IServerLevelMixin;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
//? if >1.18.1 {
//?}
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
//? if >1.16.5 {
/*import net.minecraft.core.Holder;
*///?}
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.datafix.DataFixTypes;
//? if >=1.20.1 {
/*import net.minecraft.world.RandomSequences;
*///?}

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

//? if >1.16.5 {
/*import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
*///?} else {
import net.minecraft.world.level.dimension.DimensionType;
//?}
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends MixinLevel implements IServerLevelMixin {
    @Shadow @Final private MinecraftServer server;
    //@Override
    //public PersistentEntitySectionManager<Entity> lmc$getEntityManager() {
   //     return this.entityManager;
   // }

    @Shadow
    public abstract DimensionDataStorage getDataStorage();

    //@Shadow @Final private PersistentEntitySectionManager<Entity> entityManager;

    //? if >1.16.5 {
    /*@Shadow protected abstract boolean shouldDiscardEntity(Entity arg);


    @Override
    public boolean lmc$shouldDiscardEntity(Entity entity) {
        return shouldDiscardEntity(entity);
    }
    *///?} else {
    @Override
    public boolean lmc$shouldDiscardEntity(Entity entity) {
        return this.server.isSpawningAnimals() || !(entity instanceof Animal) && !(entity instanceof WaterAnimal)
                ? !this.server.areNpcsEnabled() && entity instanceof Npc
                : true;
    }
    //?}

    @Unique
    private static final ObjectIterator<Entity> lmc$emptyEntityIter = new ObjectIterator<Entity>(){
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Entity next() {
            return null;
        }
    };

    //? if >1.16.5 {
    /*@Redirect(method = "tick",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V"))
    public void lmcEntitiesTickChunkwiseOverride(EntityTickList instance, Consumer<Entity> entity){
    *///?} else {
    @Redirect(method = "tick",at = @At(remap = false,value = "INVOKE",target = "Lit/unimi/dsi/fastutil/objects/ObjectSet;iterator()Lit/unimi/dsi/fastutil/objects/ObjectIterator;"))
    public ObjectIterator<Entity> lmcEntitiesTickChunkwiseOverride(ObjectSet<Entity> instance){
    //?}
        //noinspection resource
        if (loadMyChunks$cast().getChunkSource() instanceof ServerChunkCache) {
            ServerChunkCache scc = (ServerChunkCache) loadMyChunks$cast().getChunkSource();
            Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = ((IChunkMapMixin) scc.chunkMap).lmc$getUpdatingChunkMap();
            for (ChunkHolder value : updatingChunkMap.values()) {
                if (value != null && value.getTickingChunk() instanceof ILevelChunkMixin) {
                    ILevelChunkMixin chunk = (ILevelChunkMixin) value.getTickingChunk();
                    if (((IChunkMapMixin)scc.chunkMap).lmc$inEntityTickingRange(chunk.loadMyChunks$posAsLong())) {
                        chunk.loadMyChunks$tickEntities(getProfiler());
                    }
                }
            }
            // Effectively skips the vanilla behavior.
            //? if <1.16.6 {
            return lmc$emptyEntityIter;
            //?}
        }
        //? if <1.16.6 {
        return instance.iterator();
        //?}
    }



    //? if >1.20.5 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey<Level> resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List<CustomSpawner> list, boolean bl2, @Nullable RandomSequences randomSequences, CallbackInfo ci){
        SavedData.Factory<ChunkDataManager.LevelChunkLoaderManager> factory = new SavedData.Factory<>(()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),(tag,other)->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag), DataFixTypes.LEVEL);
        getDataStorage().computeIfAbsent(factory,"loadmychunks_manager");
    }
    *///?}

    //? if >1.20.3 && <=1.20.5 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List list, boolean bl2, RandomSequences randomSequences, CallbackInfo ci){
        SavedData.Factory<ChunkDataManager.LevelChunkLoaderManager> factory = new SavedData.Factory<>(()->ChunkDataManager.getManager(ServerLevel.class.cast(this)), tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag), DataFixTypes.LEVEL);
        getDataStorage().computeIfAbsent(factory,"loadmychunks_manager");
    }
    *///?}

    //? if >1.19.4 && <=1.20.1 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(
            MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess arg, ServerLevelData arg2, ResourceKey arg3, LevelStem arg4, ChunkProgressListener arg5, boolean bl, long l, List list, boolean bl2,
            RandomSequences arg6,
            CallbackInfo ci){
        getDataStorage().computeIfAbsent(tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag),()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }
    *///?}



    //? if >1.18.2 && <=1.19.4 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List list, boolean bl2, CallbackInfo ci){
        getDataStorage().computeIfAbsent(tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag),()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }
    *///?}

    //? if >1.18.1 && <1.18.3 {
    /*@Inject(method = "<init>", at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, Holder holder, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator, boolean bl, long l, List list, boolean bl2, CallbackInfo ci) {
        getDataStorage().computeIfAbsent((tag) -> ChunkDataManager.loadManager(ServerLevel.class.cast(this), tag), () -> ChunkDataManager.getManager(ServerLevel.class.cast(this)), "loadmychunks_manager");
    }
    *///?}

    //? if >1.16.5 && <=1.18.1 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List list, boolean bl2, RandomSequences randomSequences, CallbackInfo ci){
        getDataStorage().computeIfAbsent(tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag),()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }
    *///?}

    //? if <=1.16.5 {
    @Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, DimensionType dimensionType, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator, boolean bl, long l, List list, boolean bl2, CallbackInfo ci){
        getDataStorage().computeIfAbsent(()-> ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }
    //?}
}
