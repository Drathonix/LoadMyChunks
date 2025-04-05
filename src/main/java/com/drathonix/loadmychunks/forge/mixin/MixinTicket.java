//? if forge {
package com.drathonix.loadmychunks.forge.mixin;

import com.drathonix.loadmychunks.common.system.control.ChunkForcer;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ticket.class)
public class MixinTicket {
    @Shadow @Final private TicketType<?> type;

    @Inject(method = "isForceTicks",at=@At("HEAD"), cancellable = true)
    public void overrideLMC(CallbackInfoReturnable<Boolean> cir) {
        if(type == ChunkForcer.FORCED){
            cir.setReturnValue(true);
        }
    }
}
//?}
