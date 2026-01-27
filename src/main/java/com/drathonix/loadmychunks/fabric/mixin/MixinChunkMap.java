//? if fabric {
package com.drathonix.loadmychunks.fabric.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.IDistanceManagerMixin;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allows random ticking in playerless chunks.
 *
 * @since 1.2.0
 * @author Jack Andersen
 */
@Mixin(ChunkMap.class)
public abstract class MixinChunkMap implements IChunkMapMixin {
    // In 1.16.5 this method is called by the server chunk cache to determine if a chunk should tick.
    //? if <=1.16.5 {
    @Inject(method = "noPlayersCloseForSpawning",at = @At("HEAD"), cancellable = true)
    public void noPlayersCloseForSpawning(ChunkPos chunkPos, CallbackInfoReturnable<Boolean> cir) {
        if(((IDistanceManagerMixin)lmc$getDistanceManager()).lmc$hasEntityForcingTicket(chunkPos.toLong())){
            cir.setReturnValue(false);
        }
    }
    //?}
}
//?}
