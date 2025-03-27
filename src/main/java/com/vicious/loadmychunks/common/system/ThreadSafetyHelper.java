package com.vicious.loadmychunks.common.system;

import com.vicious.loadmychunks.common.bridge.IServerLevelMixin;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.Visibility;

/**
 * Added to avoid causing deadlocks in chunk loading due to chunk get operations.
 */
public class ThreadSafetyHelper {
    public static void forceChunk(ServerLevel level, ChunkPos pos) {
        forceChunk(level,pos,false);
    }

    public static void unforceChunk(ServerLevel level, ChunkPos pos){
        TickDelayer.delayOneTick(()->{
            level.setChunkForced(pos.x,pos.z,false);

        });
    }

    public static void forceChunk(ServerLevel level, ChunkPos pos, boolean doEntityTicking){
        TickDelayer.delayOneTick(()->{
            level.setChunkForced(pos.x,pos.z,true);
            if(doEntityTicking){
                IServerLevelMixin.getEntitySectionManager(level).updateChunkStatus(pos, Visibility.TICKING);
            }
        });
    }
}
