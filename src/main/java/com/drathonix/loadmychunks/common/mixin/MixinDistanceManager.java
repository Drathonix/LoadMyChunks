package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TickingTracker;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DistanceManager.class)
public class MixinDistanceManager implements IDistanceManagerMixin {
    @Shadow @Final private TickingTracker tickingTicketsTracker;

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
    }

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        return ITickingTrackerMixin.hasEntityForcingTicket(tickingTicketsTracker, chunkPos);
    }
}
