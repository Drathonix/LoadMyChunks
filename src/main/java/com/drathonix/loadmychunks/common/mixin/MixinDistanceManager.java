package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
//? if >1.16.5 {
import net.minecraft.server.level.TickingTracker;
//?}
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;

@Mixin(value = DistanceManager.class,priority = Integer.MAX_VALUE)
public abstract class MixinDistanceManager implements IDistanceManagerMixin {
    //? if >1.16.5 {
    @Shadow @Final private TickingTracker tickingTicketsTracker;

    //? if c2me && 1.19.2 {
    @Unique
    private IC2METickingTracker lmc$c2meNoTicksSystem = null;

    /**
     * Used to capture C2ME's NoTicksViewDistanceSystem, this way I don't have to account for their mixin.
     * @param executor
     * @param executor2
     * @param ci
     */
    @Inject(method = "<init>", at=@At("RETURN"))
    public void captureC2MEIfPresent(Executor executor, Executor executor2, CallbackInfo ci){
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (IC2METickingTracker.class.isAssignableFrom(declaredField.getType())) {
                try {
                    declaredField.setAccessible(true);
                    lmc$c2meNoTicksSystem = (IC2METickingTracker) declaredField.get(this);
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    //?}

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
    }//?} else {
    /*@Shadow @Final private Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets;
    *///?}
    @Shadow public abstract boolean hasPlayersNearby(long l);

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        //? if c2me && 1.19.2 {
        return lmc$c2meNoTicksSystem != null ? lmc$c2meNoTicksSystem.lmc$hasEntityForcingTicket(chunkPos) : ITickingTrackerMixin.hasEntityForcingTicket(tickingTicketsTracker, chunkPos);
        //?} else if >1.16.5 {
        /*return ITickingTrackerMixin.hasEntityForcingTicket(tickingTicketsTracker, chunkPos);
        *///?} else {
        /*for (Ticket<?> ticket : tickets.getOrDefault(chunkPos, SortedArraySet.create(0))) {
            if(ticket.getTicketLevel() <= ChunkForcer.ENTITY_TICKING_LEVEL){
                return true;
            }
        }
        return false;
        *///?}
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
