package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
//? if <=1.16.5 {
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.SortedArraySet;
//?}
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
//? if >1.16.5 {
/*import net.minecraft.server.level.TickingTracker;
*///?}
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;

@Mixin(value = DistanceManager.class,priority = Integer.MIN_VALUE)
public abstract class MixinDistanceManager implements IDistanceManagerMixin {
    //? if >1.16.5 {
    /*@Shadow @Final private TickingTracker tickingTicketsTracker;
    @Shadow public abstract boolean inEntityTickingRange(long l);

    @Unique
    private ITickingTrackerMixin lmc$tracker;

    @Inject(method = "<init>",at=@At("RETURN"))
    public void onConstruct(CallbackInfo ci) {
        if(!(lmc$tracker instanceof IC2METickingTracker)) {
            this.lmc$tracker = (ITickingTrackerMixin) tickingTicketsTracker;
        }
    }

    @Override
    public void lmc$overrideTracker(ITickingTrackerMixin tracker) {
        this.lmc$tracker=tracker;
    }

    @Inject(method = "addTicket(JLnet/minecraft/server/level/Ticket;)V",at=@At("TAIL"))
    public void customAdd(long l, Ticket<?> ticket, CallbackInfo ci){
        if(ticket.getType() == ChunkForcer.FORCED || ticket.getType() == ChunkForcer.ENTITY){
            //This reference is safe if C2ME is present
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
        /*return lmc$tracker.lmc$hasEntityForcingTicket(chunkPos);
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
        // Note: in 1.16.5 and older entity-ticking tickets are added to all chunks in view of a player.
        //? if >1.16.5 {
        /*return inEntityTickingRange(pos);
        *///?} else {
        return lmc$hasEntityForcingTicket(pos);
        //?}
    }

    @Override
    public boolean lmc$hasPlayersNearby(long pos) {
        return hasPlayersNearby(pos);
    }
}
