package com.vicious.loadmychunks.common.mixin.cct;

import com.vicious.loadmychunks.common.bridge.IDestroyable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TurtleBlockEntity.class,remap = false)
public class MixinTileTurtle implements IDestroyable {
    @Shadow private TurtleBrain brain;

    @Override
    public void loadMyChunks$destroy() {
        for (TurtleSide value : TurtleSide.values()) {
            IPeripheral p = brain.getPeripheral(value);
            if(p instanceof IDestroyable){
                ((IDestroyable)p).loadMyChunks$destroy();
            }
        }
    }
}
