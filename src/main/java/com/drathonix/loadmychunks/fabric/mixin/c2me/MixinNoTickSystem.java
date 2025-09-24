package com.drathonix.loadmychunks.fabric.mixin.c2me;

import org.spongepowered.asm.mixin.Mixin;
//? if fabric && c2me {
import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.ishland.c2me.notickvd.common.NoTickSystem;
import com.ishland.c2me.notickvd.common.NormalTicketDistanceMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NoTickSystem.class)
public class MixinNoTickSystem implements IC2METickingTracker {
    @Shadow(remap = false) @Final private NormalTicketDistanceMap normalTicketDistanceMap;

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        return ITickingTrackerMixin.hasEntityForcingTicket(this.normalTicketDistanceMap,chunkPos);
    }
}
//?} else {
/*@Mixin(NoTickSystem.class)
public class MixinNoTickSystem {
}
*///?}
