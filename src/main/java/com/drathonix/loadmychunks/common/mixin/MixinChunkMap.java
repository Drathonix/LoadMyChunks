package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.*;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
//? if <1.19.2 {
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
//?} else {
/*import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
*///?}
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ChunkMap.class)
public abstract class MixinChunkMap implements IChunkMapMixin {
    @Shadow @Final private Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap;

    //? if >1.16.5 {
    /*@Shadow abstract boolean anyPlayerCloseEnoughForSpawning(ChunkPos chunkPos);
    *///?}

    /**
     * method to capture the distance manager instance without using access transformers in 1.16.5 because I HATE them. I HATE THEM SO MUCH!
     */
    @Unique private IDistanceManagerMixin lmc$distanceManager;
    //? if <1.19.2 {
    @Inject(method = "<init>",at = @At(value = "RETURN"))
    public void captureDistMan(ServerLevel arg, LevelStorageSource.LevelStorageAccess arg2, DataFixer dataFixer, StructureManager arg3, Executor executor, BlockableEventLoop arg4, LightChunkGetter arg5, ChunkGenerator arg6, ChunkProgressListener arg7, Supplier supplier, int i, boolean bl, CallbackInfo ci){
    //?} else {
    /*@Inject(method = "<init>",at = @At(value = "RETURN"))
    public void captureDistMan(ServerLevel serverLevel, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer dataFixer, StructureTemplateManager structureTemplateManager, Executor executor, BlockableEventLoop blockableEventLoop, LightChunkGetter lightChunkGetter, ChunkGenerator chunkGenerator, ChunkProgressListener chunkProgressListener, ChunkStatusUpdateListener chunkStatusUpdateListener, Supplier supplier, int i, boolean bl, CallbackInfo ci){
    *///?}
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if(IDistanceManagerMixin.class.isAssignableFrom(declaredField.getType())){
                try {
                    declaredField.setAccessible(true);
                    lmc$distanceManager = (IDistanceManagerMixin) declaredField.get(this);
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Shadow @Final private ServerLevel level;


    @Override
    public Long2ObjectLinkedOpenHashMap<ChunkHolder> lmc$getUpdatingChunkMap() {
        return updatingChunkMap;
    }

    @Override
    public boolean lmc$playerDistCheck(ChunkPos pos) {
        //? if >1.16.5 {
        /*return anyPlayerCloseEnoughForSpawning(pos);
        *///?} else {
        if (!lmc$distanceManager.lmc$hasPlayersNearby(pos.toLong())) {
            return false;
        } else {
            for (ServerPlayer serverplayer : this.level.getServer().getPlayerList().getPlayers()) {
                if (this.lmc$playerIsCloseEnoughForSpawning(serverplayer, pos)) {
                    return true;
                }
            }

            return false;
        }
        //?}
    }

    @Unique
    private boolean lmc$playerIsCloseEnoughForSpawning(ServerPlayer p_183752_, ChunkPos p_183753_) {
        if (p_183752_.isSpectator()) {
            return false;
        } else {
            double d0 = lmc$euclideanDistanceSquared(p_183753_, p_183752_);
            return d0 < 16384.0;
        }
    }

    @Unique
    private static double lmc$euclideanDistanceSquared(ChunkPos p_140227_, Entity p_140228_) {
        double d0 = SectionPos.sectionToBlockCoord(p_140227_.x)+8;
        double d1 = SectionPos.sectionToBlockCoord(p_140227_.z)+8;
        double d2 = d0 - p_140228_.getX();
        double d3 = d1 - p_140228_.getZ();
        return d2 * d2 + d3 * d3;
    }

    @Override
    public boolean lmc$inEntityTickingRange(long l) {
        return lmc$distanceManager.lmc$inEntityTickingRange(l);
    }

    @Override
    public DistanceManager lmc$getDistanceManager() {
        return (DistanceManager) lmc$distanceManager;
    }
}
