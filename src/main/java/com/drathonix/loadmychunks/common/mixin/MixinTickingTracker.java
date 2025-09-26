package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

//? if >1.16.5 {
import net.minecraft.server.level.TickingTracker;
import org.spongepowered.asm.mixin.Unique;

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
            if(ticket.getTicketLevel() <= ChunkForcer.ENTITY_TICKING_LEVEL){
                return true;
            }
        }
        return false;
    }
}
//?} else {
/*@Mixin(targets="net.minecraft.server.level.DistanceManager$ChunkTicketTracker")
public abstract class MixinTickingTracker implements ITickingTrackerMixin {

}
*///?}
