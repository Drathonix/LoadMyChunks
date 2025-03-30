package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
public class MixinServerLevelEntityCallbacks {
    @Inject(method = "onTickingStart(Lnet/minecraft/world/entity/Entity;)V",at = @At("HEAD"))
    public synchronized void lmc$addToChunkTicker(Entity arg, CallbackInfo ci){
        ServerLevel level = (ServerLevel) arg.level();
        LevelChunk c = level.getChunkAt(arg.blockPosition());
        if(c instanceof ILevelChunkMixin){
            ((ILevelChunkMixin)c).lmc$addEntity(arg);
        }
    }

    @Inject(method = "onTickingEnd(Lnet/minecraft/world/entity/Entity;)V",at = @At("HEAD"))
    public synchronized void lmc$removeFromChunkTicker(Entity arg, CallbackInfo ci){
        ServerLevel level = (ServerLevel) arg.level();
        LevelChunk c = level.getChunkAt(arg.blockPosition());
        if(c instanceof ILevelChunkMixin){
            ((ILevelChunkMixin)c).lmc$removeEntity(arg);
        }
    }
}
