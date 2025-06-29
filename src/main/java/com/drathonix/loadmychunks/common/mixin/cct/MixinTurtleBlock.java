package com.drathonix.loadmychunks.common.mixin.cct;
import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
//? if cc-tweaked {

import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBlockMixin;
import com.drathonix.loadmychunks.common.registry.LMCContent;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
//? if <=1.19.2 {
/*import dan200.computercraft.shared.computer.blocks.BlockComputerBase;
*///?}
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

//? if >1.19.2 {
import dan200.computercraft.shared.turtle.blocks.TurtleBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleBlock.class)
//?} else {
/*import dan200.computercraft.shared.turtle.blocks.BlockTurtle;
@Mixin(BlockTurtle.class)
*///?}
public abstract class MixinTurtleBlock implements ITurtleBlockMixin
{
    @Override
    public void lmc$dropItems(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof IHasChunkloader){
            ((IHasChunkloader) be).ifPresent(loader->{
                if(loader.getDefaultState() == LoadStateRegistry.ENTITY_TICKING) {
                    Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(), LMCContent.itemLifeforceBroadcaster.get().getDefaultInstance());
                }
            });
        }
    }

    //? if >1.19.2 {
    @Inject(method = "onRemove",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/Containers;dropContents(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;)V"))
    public void lmc$dropAdditional(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci){
        lmc$dropItems(level,pos);
    }
    //?}
}
//?} else {
/*@Mixin(LoadMyChunks.class)
public abstract class MixinTurtleBlock {

}
*///?}
