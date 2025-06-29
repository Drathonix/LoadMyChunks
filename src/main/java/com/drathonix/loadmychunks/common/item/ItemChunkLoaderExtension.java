package com.drathonix.loadmychunks.common.item;

import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import com.drathonix.loadmychunks.common.util.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemChunkLoaderExtension extends ItemHasTooltip {
    public ItemChunkLoaderExtension(Properties properties) {
        super(properties,3);
    }

    @Override
    public InteractionResult useOnCtx(UseOnContext ctx) {
        if(ctx.getPlayer() instanceof ServerPlayer){
            BlockPos pos = ctx.getClickedPos();
            BlockEntity be = ctx.getLevel().getBlockEntity(pos);
            Message.send((ServerPlayer)ctx.getPlayer(), IHasChunkloader.map(be,loader->{
                if(loader.supportsExtensions()){
                    boolean success = loader.tryExtendBy((ServerLevel) ctx.getLevel(),1);
                    if(!success){
                        return Message.translatable("loadmychunks.chunk_loader_extension.max_extensions_reached");
                    }
                    else{
                        ItemStack stack = ctx.getItemInHand();
                        stack.shrink(1);
                        ctx.getPlayer().setItemInHand(ctx.getHand(),stack);
                        return Message.translatable("loadmychunks.chunk_loader_extension.extended_range",1, loader.getExtensionRange());
                    }
                }
                else{
                    return Message.translatable("loadmychunks.chunk_loader_extension.cannot_be_extended");
                }
            }));
            return InteractionResult.FAIL;
        }
        return super.useOnCtx(ctx);
    }

}

