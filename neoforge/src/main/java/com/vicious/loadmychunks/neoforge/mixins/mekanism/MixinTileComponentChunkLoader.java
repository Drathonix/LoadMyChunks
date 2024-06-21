package com.vicious.loadmychunks.neoforge.mixins.mekanism;

import mekanism.common.tile.component.TileComponentChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileComponentChunkLoader.class)
public class MixinTileComponentChunkLoader {
    @Inject(method = "canOperate", at= @At("RETURN"),cancellable = true)
    public void intercept(CallbackInfoReturnable<Boolean> cir){
        if(cir.getReturnValue()){
            cir.setReturnValue();
        }
    }
}
