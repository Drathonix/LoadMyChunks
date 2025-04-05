package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >1.16.5 {
/*@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
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
        if(!LoadMyChunks.stopping) {
            ServerLevel level = (ServerLevel) arg.level();
            LevelChunk c = level.getChunkAt(arg.blockPosition());
            if (c instanceof ILevelChunkMixin) {
                ((ILevelChunkMixin) c).lmc$removeEntity(arg);
            }
        }
    }
}
*///?} else {
@Mixin(ServerLevel.class)
public class MixinServerLevelEntityCallbacks {
    @Inject(method = "add",at = @At(remap=false,value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;put(ILjava/lang/Object;)Ljava/lang/Object;",ordinal = 0))
    public synchronized void lmc$addToChunkTicker(Entity arg, CallbackInfo ci){
        MultiversioningHelper.serverLevel(arg,sl->{
            LevelChunk c = sl.getChunkAt(arg.blockPosition());
            if(c instanceof ILevelChunkMixin){
                ((ILevelChunkMixin)c).lmc$addEntity(arg);
            }
        });
    }

    @Inject(method = "removeFromChunk",at = @At("HEAD"))
    public synchronized void lmc$removeFromChunkTicker(Entity entity, CallbackInfo ci){
        if(!LoadMyChunks.stopping) {
            MultiversioningHelper.serverLevel(entity,sl-> {
                LevelChunk c = sl.getChunkAt(entity.blockPosition());
                if (c instanceof ILevelChunkMixin) {
                    ((ILevelChunkMixin) c).lmc$removeEntity(entity);
                }
            });
        }
    }
}
//?}
