package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TickingTracker;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TickingTracker.class)
public class MixinTickingTracker implements ITickingTrackerMixin {
    @Shadow @Final private Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets;

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        SortedArraySet<Ticket<?>> tickets = this.tickets.get(chunkPos);
        if (tickets == null) {
            return false;
        }
        for (Ticket<?> ticket : tickets) {
            if(ticket.getType() == ChunkForcer.ENTITY){
                return true;
            }
        }
        return false;
    }
}
