package com.drathonix.loadmychunks.common.mixin.c2me;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
//? if c2me {
/*import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import net.minecraft.server.level.DistanceManager;
import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import com.drathonix.loadmychunks.common.bridge.ITickingTrackerMixin;
import com.ishland.c2me.notickvd.common.NoTickSystem;
import com.ishland.c2me.notickvd.common.NormalTicketDistanceMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^*
 * Allows getting the C2ME Chunk Ticket Map and using that instead of the default. This allows random ticking in LMC loaded chunks.
 *
 * @author Jack Andersen
 * @since 1.2.0
 ^/
@Mixin(NoTickSystem.class)
public class MixinNoTickSystem implements IC2METickingTracker {
    @Shadow(remap = false) @Final private NormalTicketDistanceMap normalTicketDistanceMap;

    @Inject(method = "<init>",at = @At("RETURN"))
    public void intercept(DistanceManager chunkTicketManager, CallbackInfo ci){
        IDistanceManagerMixin.overrideTracker(chunkTicketManager,this);
    }

    @Override
    public boolean lmc$hasEntityForcingTicket(long chunkPos) {
        return ITickingTrackerMixin.hasEntityForcingTicket(this.normalTicketDistanceMap,chunkPos);
    }
}
*///?} else {
@Mixin(LoadMyChunks.class)
public class MixinNoTickSystem {
}
//?}
