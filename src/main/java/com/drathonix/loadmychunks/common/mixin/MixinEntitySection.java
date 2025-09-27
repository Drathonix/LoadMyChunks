package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IEntitySectionMixin;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
/*import net.minecraft.world.level.chunk.status.ChunkStatus;
*///?} else {
import net.minecraft.world.level.chunk.ChunkStatus;
//?}
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;

@Mixin(EntitySection.class)
public class MixinEntitySection implements IEntitySectionMixin {
    @Unique
    private long lmc$longChunk;

    @Inject(method = "add",at = @At("TAIL"))
    public <T extends EntityAccess> void addToChunkTicker(T entityAccess, CallbackInfo ci){
        try{
            if(entityAccess instanceof Entity){
                Entity e = (Entity) entityAccess;
                MultiversioningHelper.serverLevel(e,sl->{
                    ChunkDataManager.getOrCreateChunkData(sl, lmc$longChunk).lmc$addEntity(e);
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Inject(method = "remove",at = @At("TAIL"))
    public <T extends EntityAccess> void removeFromChunkTicker(T entityAccess, CallbackInfoReturnable<Boolean> cir){
        try {
            if (entityAccess instanceof Entity) {
                Entity e = (Entity) entityAccess;
                MultiversioningHelper.serverLevel(e, sl -> {
                    ChunkDataManager.getOrCreateChunkData(sl, lmc$longChunk).lmc$removeEntity(e);
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void lmc$setChunkPos(long pos) {
        lmc$longChunk=pos;
    }
}
//?} else {
/*import com.drathonix.loadmychunks.common.LoadMyChunks;
@Mixin(LoadMyChunks.class)
public class MixinEntitySection{

}
*///?}
