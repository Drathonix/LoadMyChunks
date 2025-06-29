package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IServerChunkCacheMixin;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.ChunkAccess;
//? if >1.20.4 {
import net.minecraft.world.level.chunk.status.ChunkStatus;
//?} else {
/*import net.minecraft.world.level.chunk.ChunkStatus;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Mixin(ServerChunkCache.class)
public abstract class MixinServerChunkCache implements IServerChunkCacheMixin {
    @Unique
    private static final Executor lmc$asyncExecutor = Executors.newSingleThreadExecutor();

    @Shadow protected abstract CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFutureMainThread(int i, int j, ChunkStatus chunkStatus, boolean bl);

    @Override
    public CompletableFuture<Either<ChunkAccess, ?>> lmc$getChunkAsync(int i, int j, ChunkStatus arg, boolean bl) {
        return CompletableFuture.supplyAsync(() -> {
            return this.getChunkFutureMainThread(i, j, arg, bl);
        }, lmc$asyncExecutor).thenCompose((completableFuture) -> {
            return completableFuture;
        });
    }
}
