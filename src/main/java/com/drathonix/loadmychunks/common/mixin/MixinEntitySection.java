package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.18.2 {
import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
//? if >1.20.4 {
import net.minecraft.world.level.chunk.status.ChunkStatus;
//?} else {
/*import net.minecraft.world.level.chunk.ChunkStatus;
*///?}
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;

@Mixin(EntitySection.class)
public class MixinEntitySection {
    @Inject(method = "add",at = @At("TAIL"))
    public <T extends EntityAccess> void addToChunkTicker(T entityAccess, CallbackInfo ci){
        if(entityAccess instanceof Entity){
            Entity e = (Entity) entityAccess;
            MultiversioningHelper.serverLevel(e,sl->{
                ChunkPos cpos = MultiversioningHelper.chunkPosOf(e);
                ChunkAccess c = sl.getChunkSource().getChunk(cpos.x,cpos.z,ChunkStatus.FULL,false);
                if(c instanceof ILevelChunkMixin){
                    ((ILevelChunkMixin) c).lmc$addEntity(e);
                } else {
                    ChunkDataManager.waitForChunkInit(sl, new ChunkPos(cpos.x, cpos.z), cdm -> {
                        cdm.getChunk().lmc$addEntity(e);
                    });
                }
            });
        }
    }

    @Inject(method = "remove",at = @At("TAIL"))
    public <T extends EntityAccess> void removeFromChunkTicker(T entityAccess, CallbackInfoReturnable<Boolean> cir){
        if(entityAccess instanceof Entity){
            Entity e = (Entity) entityAccess;
            MultiversioningHelper.serverLevel(e,sl->{
                ChunkPos cpos = MultiversioningHelper.chunkPosOf(e);
                sl.getChunkSource().getChunkFuture(cpos.x,cpos.z,ChunkStatus.FULL,true).handleAsync((ca,th)->{
                    return ca.mapLeft(c->{
                        if(c instanceof ILevelChunkMixin){
                            ((ILevelChunkMixin) c).lmc$removeEntity(e);
                        }
                        return null;
                    });
                });
            });
        }
    }
}
//?} else {
/*@Mixin(LoadMyChunks.class)
public class MixinEntitySection{

}
*///?}
