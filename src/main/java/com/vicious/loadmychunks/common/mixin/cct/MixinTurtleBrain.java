package com.vicious.loadmychunks.common.mixin.cct;

//? if !cct {
import com.vicious.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTurtleBrain {

}
//?}
//? if cct {

/*import com.vicious.loadmychunks.common.bridge.IContextDestroyable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
//? if >=1.20.6
import dan200.computercraft.core.util.PeripheralHelpers;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TurtleBrain.class,remap = false)
public class MixinTurtleBrain {
    //? <1.20.6 {
    /^@Inject(method = "updatePeripherals",at = @At(value = "INVOKE",target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILSOFT)
    public void intercept(ServerComputer serverComputer, CallbackInfo ci, TurtleSide[] var2, int var3, int var4, TurtleSide side, ITurtleUpgrade upgrade, IPeripheral peripheral, IPeripheral existing){
        if(existing instanceof IContextDestroyable){
            ((IContextDestroyable) existing).loadMyChunks$destroy(existing);
        }
    }
    ^///?}

    //? >=1.20.6 {
    @Redirect(method = "updatePeripherals",at = @At(value = "INVOKE",target = "Ldan200/computercraft/core/util/PeripheralHelpers;equals(Ldan200/computercraft/api/peripheral/IPeripheral;Ldan200/computercraft/api/peripheral/IPeripheral;)Z"))
    public boolean intercept(IPeripheral a, IPeripheral b){
        if(!PeripheralHelpers.equals(a,b)) {
            if (a instanceof IContextDestroyable) {
                ((IContextDestroyable) a).loadMyChunks$destroy(a);
            }
            return false;
        }
        return true;
    }
    //?}
}
*///?}
