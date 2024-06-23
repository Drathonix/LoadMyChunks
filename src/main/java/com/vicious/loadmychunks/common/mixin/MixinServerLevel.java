package com.vicious.loadmychunks.common.mixin;

import com.vicious.loadmychunks.common.system.ChunkDataManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {
    @Shadow public abstract DimensionDataStorage getDataStorage();

    //? if >1.20.5 {
    @Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey<Level> resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List<CustomSpawner> list, boolean bl2, @Nullable RandomSequences randomSequences, CallbackInfo ci){
        SavedData.Factory<ChunkDataManager.LevelChunkLoaderManager> factory = new SavedData.Factory<>(()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),(tag,other)->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag), DataFixTypes.LEVEL);
        getDataStorage().computeIfAbsent(factory,"loadmychunks_manager");
    }
    //?}

    //? if >1.20.3 && <=1.20.5 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List list, boolean bl2, RandomSequences randomSequences, CallbackInfo ci){
        SavedData.Factory<ChunkDataManager.LevelChunkLoaderManager> factory = new SavedData.Factory<>(()->ChunkDataManager.getManager(ServerLevel.class.cast(this)), tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag), DataFixTypes.LEVEL);
        getDataStorage().computeIfAbsent(factory,"loadmychunks_manager");
    }
    *///?}

    //? if >1.16.5 && <=1.20.3 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, LevelStem levelStem, ChunkProgressListener chunkProgressListener, boolean bl, long l, List list, boolean bl2, RandomSequences randomSequences, CallbackInfo ci){
        getDataStorage().computeIfAbsent(tag->ChunkDataManager.loadManager(ServerLevel.class.cast(this),tag),()->ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }

    *///?}

    //? if <=1.16.5 {
    /*@Inject(method = "<init>",at = @At("RETURN"))
    public void injectCustomSaveData(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, DimensionType dimensionType, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator, boolean bl, long l, List list, boolean bl2, CallbackInfo ci){
        getDataStorage().computeIfAbsent(()-> ChunkDataManager.getManager(ServerLevel.class.cast(this)),"loadmychunks_manager");
    }
    *///?}

    //@Inject(method = "setChunkForced",at = "")


}
