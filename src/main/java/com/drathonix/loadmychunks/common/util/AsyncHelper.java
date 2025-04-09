package com.drathonix.loadmychunks.common.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

import java.util.function.Consumer;

/**
 * @since 1.2.0
 * @author Jack Andersen
 */
public class AsyncHelper {
    public static void accessChunk(ServerLevel sl, ChunkPos pos, Consumer<ChunkAccess> cons){
        accessChunk(sl,pos.x,pos.z,cons);
    }

    public static void accessChunk(ServerLevel sl, int x, int z, Consumer<ChunkAccess> cons){
        sl.getChunkSource().getChunkFuture(x,z, ChunkStatus.FULL,true).handleAsync((either,th)->{
            either.mapLeft(c->{
                cons.accept(c);
                return null;
            });
            return th;
        });
    }
}
