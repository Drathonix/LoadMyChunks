package com.drathonix.loadmychunks.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.drathonix.loadmychunks.common.LoadMyChunks;
//? if neoforge && >=1.21.2 {

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

/**
 * Allows LMC to enable random ticks in entity ticking forced chunks.
 * Normally vanilla does not randomly tick when a chunk is forced. This behavior is maintained except for LMC chunkloading.
 * @since 1.2.0
 * @author Jack Andersen
 */
@Mixin(ServerChunkCache.class)
public class MixinServerChunkCache {

    //? if >=1.21.2 {
    @Shadow @Final private DistanceManager distanceManager;
    @Redirect(method = "collectTickingChunks",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/level/ChunkMap;forEachSpawnCandidateChunk(Ljava/util/function/Consumer;)V"))
    public void doNotCareAboutPlayerDist(ChunkMap instance, Consumer<ChunkHolder> consumer){
        ((IChunkMapMixin)instance).lmc$getUpdatingChunkMap().forEach((inst,holder)->{
            if(((IChunkMapMixin)instance).lmc$playerDistCheck(holder.getPos()) ||
                IDistanceManagerMixin.lmc$hasEntityForcingTicket(distanceManager,inst)){
                consumer.accept(holder);
            }
        });
    }
    //?}
}

//?} else {
/*@Mixin(LoadMyChunks.class)
public class MixinServerChunkCache {
}
*///?}