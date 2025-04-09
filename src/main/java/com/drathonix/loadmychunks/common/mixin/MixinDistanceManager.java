package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
//? if >1.16.5 {
/*import net.minecraft.server.level.TickingTracker;
*///?}
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DistanceManager.class)
public abstract class MixinDistanceManager implements IDistanceManagerMixin {
    //? if >1.16.5 {
    /*@Shadow @Final private TickingTracker tickingTicketsTracker;

    @Inject(method = "addTicket(JLnet/minecraft/server/level/Ticket;)V",at=@At("TAIL"))
    public void customAdd(long l, Ticket<?> ticket, CallbackInfo ci){
        if(ticket.getType() == ChunkForcer.FORCED || ticket.getType() == ChunkForcer.ENTITY){
            tickingTicketsTracker.addTicket(l,ticket);
        }
    }

    @Inject(method = "removeTicket(JLnet/minecraft/server/level/Ticket;)V",at=@At("TAIL"))
    public void customRemove(long l, Ticket<?> ticket, CallbackInfo ci){
        if(ticket.getType() == ChunkForcer.FORCED || ticket.getType() == ChunkForcer.ENTITY){
            tickingTicketsTracker.removeTicket(l,ticket);
        }
    }*///?} else {
    @Shadow @Final private Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets;
    //?}
    @Shadow public abstract boolean hasPlayersNearby(long l);

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        //? if >1.16.5 {
        /*return ITickingTrackerMixin.hasEntityForcingTicket(tickingTicketsTracker, chunkPos);
        *///?} else {
        for (Ticket<?> ticket : tickets.getOrDefault(chunkPos, SortedArraySet.create(0))) {
            if(ticket.getTicketLevel() <= ChunkForcer.ENTITY_TICKING_LEVEL){
                return true;
            }
        }
        return false;
        //?}
    }


    @Override
    public boolean lmc$inEntityTickingRange(long pos) {
        return lmc$hasEntityForcingTicket(pos);
    }

    @Override
    public boolean lmc$hasPlayersNearby(long pos) {
        return hasPlayersNearby(pos);
    }
}
