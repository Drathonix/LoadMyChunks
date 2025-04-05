package com.drathonix.loadmychunks.common.mixin.cct;

import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBlockMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
//? if cc-tweaked {
import dan200.computercraft.shared.common.BlockGeneric;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGeneric.class)
public class MixinBlockGeneric {
    @Inject(method = "onRemove",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;onRemove(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V"))
    public void interceptAndDrop(BlockState block, Level world, BlockPos pos, BlockState replace, boolean bool, CallbackInfo ci){
        if(this instanceof ITurtleBlockMixin){
            ((ITurtleBlockMixin) this).lmc$dropItems(world,pos);
        }
    }
}
//?} else {
/*@Mixin(LoadMyChunks.class)
public class MixinBlockComputerBase {
}
*///?}
