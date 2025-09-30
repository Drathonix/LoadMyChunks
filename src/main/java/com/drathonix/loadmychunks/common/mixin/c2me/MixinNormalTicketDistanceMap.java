package com.drathonix.loadmychunks.common.mixin.c2me;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
//? if c2me {
/*import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.Ticket;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Final;
import com.ishland.c2me.notickvd.common.NormalTicketDistanceMap;
import org.spongepowered.asm.mixin.Shadow;

/^*
 * Helper mixin for entity ticking in LMC chunks.
 *
 * @author Jack Andersen
 * @since 1.2.0
 ^/
@Mixin(NormalTicketDistanceMap.class)
public abstract class MixinNormalTicketDistanceMap implements ITickingTrackerMixin {
    @Shadow(remap = false) @Final private Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> ticketsByPosition;

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        SortedArraySet<Ticket<?>> tickets = this.ticketsByPosition.get(chunkPos);
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
*///?} else {
@Mixin(LoadMyChunks.class)
public class MixinNormalTicketDistanceMap {

}
//?}
