package com.drathonix.loadmychunks.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >1.16.5 {
import com.drathonix.loadmychunks.common.bridge.IEntitySectionMixin;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import net.minecraft.world.level.entity.Visibility;
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

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(EntitySection.class)
public abstract class MixinEntitySection<T extends EntityAccess> implements IEntitySectionMixin {
    @Shadow public abstract Stream<T> getEntities();

    @Unique
    private long lmc$longChunk;

    @Inject(method = "add",at = @At("TAIL"))
    public void addToChunkTicker(T entityAccess, CallbackInfo ci){

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
    public void removeFromChunkTicker(T entityAccess, CallbackInfoReturnable<Boolean> cir){
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

    // This is only called when either the chunk holding the ent section is fully unloaded or enters full/block ticking/entity ticking status.
    // Without this mixin, when a section is loaded for the first time everything works as intended, however when the chunks containing said section become inaccessible this entity section persists. When the chunk reloads the entities contained in this section are not re-added to the lmc ticker.
    @Inject(method = "updateChunkStatus",at = @At("HEAD"))
    public void captureEntitiesAgain(Visibility visibility, CallbackInfoReturnable<Visibility> cir){
        Optional<T> e = getEntities().findFirst();
        if(e.isPresent() && visibility.isTicking()) {
            MultiversioningHelper.serverLevel((Entity)e.get(),sl->{
                ChunkDataModule module = ChunkDataManager.getOrCreateChunkData(sl, lmc$longChunk);
                getEntities().forEach(ent -> {
                    module.lmc$addEntity((Entity)ent);
                });
            });
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
