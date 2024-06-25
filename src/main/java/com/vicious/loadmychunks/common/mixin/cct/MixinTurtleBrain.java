package com.vicious.loadmychunks.common.mixin.cct;

import com.vicious.loadmychunks.common.bridge.IDestroyable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.util.PeripheralHelpers;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TurtleBrain.class,remap = false)
public class MixinTurtleBrain {
    @Redirect(method = "updatePeripherals",at = @At(value = "INVOKE", target = "Ldan200/computercraft/core/util/PeripheralHelpers;equals(Ldan200/computercraft/api/peripheral/IPeripheral;Ldan200/computercraft/api/peripheral/IPeripheral;)Z"))
    public boolean intercept(IPeripheral a, IPeripheral b){
        if(PeripheralHelpers.equals(a,b)){
            return true;
        }
        if(a instanceof IDestroyable){
            ((IDestroyable) a).loadMyChunks$destroy();
        }
        return false;
    }
}
