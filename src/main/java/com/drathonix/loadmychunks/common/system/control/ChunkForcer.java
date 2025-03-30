package com.drathonix.loadmychunks.common.system.control;

import net.minecraft.server.commands.ForceLoadCommand;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.NetherPortalBlock;

import java.util.Comparator;

public class ChunkForcer {
    // Putting this here for reference
    // Any level below or equal to 31 is entity ticking, 32 is block ticking, 33 is full but not loaded, 34+ is not full.
    // private static final int FULL_CHUNK_LEVEL = 33;
    private static final int BLOCK_TICKING_LEVEL = 32;
    private static final int ENTITY_TICKING_LEVEL = 31;

    public static final TicketType<ChunkPos> FORCED = TicketType.create("lmc_forced", Comparator.comparingLong(ChunkPos::toLong));
    public static final TicketType<ChunkPos> ENTITY = TicketType.create("lmc_entity", Comparator.comparingLong(ChunkPos::toLong));
    private synchronized static void addTicket(DistanceManager manager, ChunkPos pos, boolean entityTicking){
        //Remove the inverted ticket if it is present.
        removeTicket(manager,pos, !entityTicking);
        manager.addTicket(entityTicking ? ENTITY : FORCED, pos, entityTicking ? ENTITY_TICKING_LEVEL : BLOCK_TICKING_LEVEL, pos);
    }
    private synchronized static void removeTicket(DistanceManager manager, ChunkPos pos, boolean entityTicking){
        manager.addTicket(entityTicking ? ENTITY : FORCED, pos, entityTicking ? ENTITY_TICKING_LEVEL : BLOCK_TICKING_LEVEL, pos);
    }

    public static void unforceChunk(ServerLevel level, ChunkPos pos, boolean wasEntityTicking){
        removeTicket(level.getChunkSource().chunkMap.getDistanceManager(),pos,wasEntityTicking);
    }

    public static void forceChunk(ServerLevel level, ChunkPos pos, boolean doEntityTicking){
        addTicket(level.getChunkSource().chunkMap.getDistanceManager(),pos,doEntityTicking);
    }
}
