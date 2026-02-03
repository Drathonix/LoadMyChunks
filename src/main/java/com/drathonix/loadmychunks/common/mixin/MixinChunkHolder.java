package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

//? if <=1.16.5 {
/*@Mixin(ChunkHolder.class)
public abstract class MixinChunkHolder {
    @Shadow
    public static ChunkHolder.FullChunkStatus getFullChunkStatus(int i) {
        return null;
    }

    @Shadow
    private volatile CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> entityTickingChunkFuture;

    @Redirect(method = "updateFutures",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/level/ChunkHolder$LevelChangeListener;onLevelChange(Lnet/minecraft/world/level/ChunkPos;Ljava/util/function/IntSupplier;ILjava/util/function/IntConsumer;)V"))
    public void onStatusChange(ChunkHolder.LevelChangeListener instance, ChunkPos chunkPos, IntSupplier intSupplier, int i, IntConsumer intConsumer){
        instance.onLevelChange(chunkPos,intSupplier,i,intConsumer);
        ChunkHolder.FullChunkStatus fullChunkStatus2 = getFullChunkStatus(i);
        if(fullChunkStatus2.isOrAfter(ChunkHolder.FullChunkStatus.ENTITY_TICKING)){
            entityTickingChunkFuture.acceptEitherAsync(entityTickingChunkFuture, c->{
                c.ifLeft(chunk->{
                    ((ILevelChunkMixin)chunk).loadMyChunks$1165reloadEntities();
                });
            });
        }
    }
}
*///?} else {
import com.drathonix.loadmychunks.common.LoadMyChunks;

@Mixin(LoadMyChunks.class)
public class MixinChunkHolder {
}
//?}
