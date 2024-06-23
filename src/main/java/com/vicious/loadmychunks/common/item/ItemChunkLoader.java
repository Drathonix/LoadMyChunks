package com.vicious.loadmychunks.common.item;

import net.minecraft.network.chat.Component;
//? if <1.18.3
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

    //? if <=1.20.5 {
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        //? if <1.18.3
        /*list.add(new TranslatableComponent(getTooltipTranslationKey()));*/
        //? if >1.18.2
        list.add(Component.translatable(getTooltipTranslationKey()));
    }
    //?}

    //? if >1.20.5 {
    /*@Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable(getTooltipTranslationKey()));
    }
    *///?}


    public String getTooltipTranslationKey() {
        return "tooltip.loadmychunks.chunk_loader";
    }
}
