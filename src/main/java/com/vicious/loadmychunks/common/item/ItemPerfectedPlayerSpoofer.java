package com.vicious.loadmychunks.common.item;

import com.vicious.loadmychunks.common.system.loaders.IHasChunkloader;
import com.vicious.loadmychunks.common.util.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemPerfectedPlayerSpoofer extends ItemHasTooltip {
    public ItemPerfectedPlayerSpoofer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if(useOnContext.getPlayer() instanceof ServerPlayer){
            BlockPos pos = useOnContext.getClickedPos();
            BlockEntity be = useOnContext.getLevel().getBlockEntity(pos);
            if(be instanceof IHasChunkloader && ((IHasChunkloader)be).supportsEntityTicking()){
                boolean extended = ((IHasChunkloader) be).loadMyChunks$extendRange(1);
                if(!extended){
                    if(useOnContext.getPlayer() instanceof ServerPlayer) {
                        Message.send((ServerPlayer)useOnContext.getPlayer(),Message.translatable("loadmychunks.chunk_loader_extension.max_extensions_reached"));
                    }
                    return InteractionResult.FAIL;
                }
                else{
                    if(useOnContext.getPlayer() instanceof ServerPlayer) {
                        Message.send((ServerPlayer)useOnContext.getPlayer(),Message.translatable("loadmychunks.chunk_loader_extension.extended_range",1,((IHasChunkloader) be).loadMyChunks$getChunkLoader().getExtensionRange()));
                    }
                }
                ItemStack stack = useOnContext.getItemInHand();
                stack.shrink(1);
                useOnContext.getPlayer().setItemInHand(useOnContext.getHand(),stack);
            }
            else{
                Message.send((ServerPlayer)useOnContext.getPlayer(),Message.translatable("loadmychunks.chunk_loader_extension.cannot_be_extended"));
            }
            return InteractionResult.FAIL;
        }
        return super.useOn(useOnContext);
    }

}

