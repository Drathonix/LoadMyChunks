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
    public InteractionResult useOn(UseOnContext useOnContext) {
        if(useOnContext.getPlayer() instanceof ServerPlayer){
            BlockPos pos = useOnContext.getClickedPos();
            BlockEntity be = useOnContext.getLevel().getBlockEntity(pos);
            Message.send((ServerPlayer)useOnContext.getPlayer(), IHasChunkloader.map(be,loader->{
                if(loader.supportsExtensions()){
                    boolean success = loader.tryExtendBy((ServerLevel) useOnContext.getLevel(),1);
                    if(!success){
                        return Message.translatable("loadmychunks.chunk_loader_extension.max_extensions_reached");
                    }
                    else{
                        ItemStack stack = useOnContext.getItemInHand();
                        stack.shrink(1);
                        useOnContext.getPlayer().setItemInHand(useOnContext.getHand(),stack);
                        return Message.translatable("loadmychunks.chunk_loader_extension.extended_range",1, loader.getExtensionRange());
                    }
                }
                else{
                    return Message.translatable("loadmychunks.chunk_loader_extension.cannot_be_extended");
                }
            }));
            return InteractionResult.FAIL;
        }
        return super.useOn(useOnContext);
    }

}

