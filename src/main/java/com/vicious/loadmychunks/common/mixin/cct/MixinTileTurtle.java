package com.vicious.loadmychunks.common.mixin.cct;

//? if !cct {
/*import com.vicious.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTileTurtle {

}
*///?}
//? if cct {

import com.vicious.loadmychunks.common.bridge.IContextDestroyable;
import com.vicious.loadmychunks.common.bridge.IDestroyable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
//? if >1.19.2
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//? if <=1.19.2
/*import dan200.computercraft.shared.turtle.blocks.TileTurtle;*/
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if >1.19.2
@Mixin(value = TurtleBlockEntity.class,remap = false)
//? if <=1.19.2
/*@Mixin(value = TileTurtle.class,remap = false)*/
public class MixinTileTurtle implements IDestroyable {
    @Shadow
    private TurtleBrain brain;

    @Override
    public void loadMyChunks$destroy() {
        if(!BlockEntity.class.cast(this).getLevel().isClientSide()) {
            for (TurtleSide value : TurtleSide.values()) {
                IPeripheral p = brain.getPeripheral(value);
                if (p instanceof IContextDestroyable) {
                    ((IContextDestroyable) p).loadMyChunks$destroy(this);
                }
            }
        }
    }
}
//?}
