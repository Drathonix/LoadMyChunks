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

public class ItemLifeforceBroadcaster extends ItemHasTooltip {
    public ItemLifeforceBroadcaster(Properties properties) {
        super(properties,4);
    }

    @Override
    public InteractionResult useOnCtx(UseOnContext useOnContext) {
        if(useOnContext.getPlayer() instanceof ServerPlayer){
            BlockPos pos = useOnContext.getClickedPos();
            BlockEntity be = useOnContext.getLevel().getBlockEntity(pos);
            Message.send((ServerPlayer)useOnContext.getPlayer(), IHasChunkloader.map(be,loader->{
                if(loader.supportsEntityTicking()){
                    boolean success = loader.enableEntityTicking((ServerLevel) useOnContext.getLevel());
                    if(!success){
                        return Message.translatable("loadmychunks.upgrade.already_installed");
                    }
                    else{
                        ItemStack stack = useOnContext.getItemInHand();
                        stack.shrink(1);
                        useOnContext.getPlayer().setItemInHand(useOnContext.getHand(),stack);
                        return Message.translatable("loadmychunks.lifeforce_broadcaster.entity_ticking_enabled");
                    }
                }
                else{
                    return Message.translatable("loadmychunks.lifeforce_broadcaster.cannot_entity_tick");
                }
            }));
            return InteractionResult.FAIL;
        }
        return super.useOnCtx(useOnContext);
    }

}

