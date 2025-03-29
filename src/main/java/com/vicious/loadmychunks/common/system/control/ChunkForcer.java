package com.vicious.loadmychunks.common.system.control;

import com.vicious.loadmychunks.common.bridge.IServerLevelMixin;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.Visibility;

import java.util.Comparator;

public class ChunkForcer {
    private static final int level = 0;
    public static final TicketType<ChunkPos> FORCED = TicketType.create("lmc_forced", Comparator.comparingLong(ChunkPos::toLong));
    public static final TicketType<ChunkPos> ENTITY = TicketType.create("lmc_entity", Comparator.comparingLong(ChunkPos::toLong));
    private synchronized static void addTicket(DistanceManager manager, ChunkPos pos, boolean entityTicking){
        manager.addTicket(entityTicking ? ENTITY : FORCED,pos,level,pos);
    }
    private synchronized static void removeTicket(DistanceManager manager, ChunkPos pos, boolean entityTicking){
        manager.removeTicket(entityTicking ? ENTITY : FORCED,pos,level,pos);
    }

    public static void forceChunk(ServerLevel level, ChunkPos pos) {
        forceChunk(level,pos,false);
    }

    public static void unforceChunk(ServerLevel level, ChunkPos pos, boolean wasEntityTicking){
        removeTicket(level.getChunkSource().chunkMap.getDistanceManager(),pos,wasEntityTicking);
    }

    public static void forceChunk(ServerLevel level, ChunkPos pos, boolean doEntityTicking){
        addTicket(level.getChunkSource().chunkMap.getDistanceManager(),pos,doEntityTicking);
    }
}
