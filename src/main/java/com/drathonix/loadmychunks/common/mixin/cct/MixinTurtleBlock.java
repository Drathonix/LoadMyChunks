package com.drathonix.loadmychunks.common.mixin.cct;

import com.drathonix.loadmychunks.common.registry.LMCContent;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import dan200.computercraft.shared.turtle.blocks.TurtleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TurtleBlock.class)
public class MixinTurtleBlock {
    @Redirect(method="onRemove",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/Containers;dropContents(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;)V"))
    public void lmc$dropAdditional(Level level, BlockPos pos, Container container){
        Containers.dropContents(level,pos,container);
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof IHasChunkloader){
            ((IHasChunkloader) be).ifPresent(loader->{
                if(loader.getDefaultState() == LoadStateRegistry.ENTITY_TICKING) {
                    Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(), LMCContent.itemLifeforceBroadcaster.get().getDefaultInstance());
                }
            });
        }
    }
}
