package com.drathonix.loadmychunks.fabric.mixin.c2me;

import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import net.minecraft.server.level.DistanceManager;
import org.spongepowered.asm.mixin.Mixin;
//? if fabric && c2me {
import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.ishland.c2me.notickvd.common.NoTickSystem;
import com.ishland.c2me.notickvd.common.NormalTicketDistanceMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoTickSystem.class)
public class MixinNoTickSystem implements IC2METickingTracker {
    @Shadow(remap = false) @Final private NormalTicketDistanceMap normalTicketDistanceMap;

    @Inject(method = "<init>",at = @At("RETURN"))
    public void intercept(DistanceManager chunkTicketManager, CallbackInfo ci){
        IDistanceManagerMixin.lmc$setC2MENTS(chunkTicketManager,this);
    }

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
