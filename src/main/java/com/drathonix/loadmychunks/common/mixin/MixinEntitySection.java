package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.bridge.IServerChunkCacheMixin;
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
                System.out.println("Executing E-ADD");
                IServerChunkCacheMixin.getChunkAsync(sl.getChunkSource(),cpos.x,cpos.z,ChunkStatus.FULL,true).handleAsync((ca, th)->
                    //? if >1.20.4 {
                    ca.map(c->{
                    //?} else {
                    /*ca.mapLeft(c->{
                    *///?}
                    if(c instanceof ILevelChunkMixin){
                        ((ILevelChunkMixin) c).lmc$addEntity(e);
                    }
                    return null;
                }));
                System.out.println("Post E-ADD Request");
            });
        }
    }

    @Inject(method = "remove",at = @At("TAIL"))
    public <T extends EntityAccess> void removeFromChunkTicker(T entityAccess, CallbackInfoReturnable<Boolean> cir){
        if(entityAccess instanceof Entity){
            Entity e = (Entity) entityAccess;
            MultiversioningHelper.serverLevel(e,sl->{
                ChunkPos cpos = MultiversioningHelper.chunkPosOf(e);
                sl.getChunkSource().getChunkFuture(cpos.x,cpos.z, ChunkStatus.FULL,true).handleAsync((ca,th)->
                    //? if >1.20.4 {
                    ca.map(c->{
                     //?} else {
                    /*ca.mapLeft(c->{
                    *///?}
                    if(c instanceof ILevelChunkMixin){
                        ((ILevelChunkMixin) c).lmc$removeEntity(e);
                    }
                    return null;
                }));
            });
        }
    }
}
//?} else {
/*@Mixin(LoadMyChunks.class)
public class MixinEntitySection{

}
*///?}
