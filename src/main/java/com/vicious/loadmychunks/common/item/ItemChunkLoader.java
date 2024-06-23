package com.vicious.loadmychunks.common.item;

import net.minecraft.network.chat.Component;
//? if <=1.16.5
/*import net.minecraft.network.chat.TranslatableComponent;*/
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemChunkLoader extends BlockItem {
    public ItemChunkLoader(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        //? if <=1.16.5
        /*list.add(new TranslatableComponent(getTooltipTranslationKey()));*/
        //? if >1.16.5
        list.add(Component.translatable(getTooltipTranslationKey()));
    }

    public String getTooltipTranslationKey() {
        return "tooltip.loadmychunks.chunk_loader";
    }
}