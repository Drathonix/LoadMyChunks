package com.drathonix.loadmychunks.common.bridge;

import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ServerChunkCache;
//? if >1.20.4 {
import net.minecraft.world.level.chunk.status.ChunkStatus;
//?} else {
/*import net.minecraft.world.level.chunk.ChunkStatus;
 *///?}
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.concurrent.CompletableFuture;

public interface IServerChunkCacheMixin {
    CompletableFuture<Either<ChunkAccess, ?>> lmc$getChunkAsync(int i, int j, ChunkStatus arg, boolean bl);

    static CompletableFuture<Either<ChunkAccess, ?>> getChunkAsync(ServerChunkCache src, int i, int j, ChunkStatus arg, boolean bl){
        if(src instanceof IServerChunkCacheMixin) {
            return ((IServerChunkCacheMixin) src).lmc$getChunkAsync(i, j, arg, bl);
        } else {
            throw new IllegalStateException("IServerChunkCacheMixin not applied!");
        }
    }

}
