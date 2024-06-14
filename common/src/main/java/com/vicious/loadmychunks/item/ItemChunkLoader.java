package com.vicious.loadmychunks.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ItemChunkLoader extends BlockItem {
    public ItemChunkLoader(Block block, Properties properties) {
        super(block, properties);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable(getTooltipTranslationKey()));
    }

    public String getTooltipTranslationKey() {
        return "tooltip.loadmychunks.chunk_loader";
    }
}
