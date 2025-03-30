package com.drathonix.loadmychunks.fabric.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkCache.class)
public class MixinServerChunkCache {
    @Shadow @Final private DistanceManager distanceManager;

    @Redirect(method="tickChunks",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;anyPlayerCloseEnoughForSpawning(Lnet/minecraft/world/level/ChunkPos;)Z"))
    public boolean doNotCareAboutPlayerDist(ChunkMap instance, ChunkPos chunkPos){
        if(((IChunkMapMixin)instance).lmc$playerDistCheck(chunkPos)){
            return true;
        }
        return IDistanceManagerMixin.lmc$hasEntityForcingTicket(distanceManager,chunkPos.toLong());
    }
}
