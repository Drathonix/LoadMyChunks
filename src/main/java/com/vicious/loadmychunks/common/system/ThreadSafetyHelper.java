package com.vicious.loadmychunks.common.system;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * Added to avoid causing deadlocks in chunk loading due to chunk get operations.
 */
public class ThreadSafetyHelper {
    public static void forceChunk(ServerLevel level, ChunkPos pos){
        TickDelayer.delayOneTick(()->level.setChunkForced(pos.x,pos.z,true));
    }

    public static void unforceChunk(ServerLevel level, ChunkPos pos){
        TickDelayer.delayOneTick(()->level.setChunkForced(pos.x,pos.z,false));
    }
}
