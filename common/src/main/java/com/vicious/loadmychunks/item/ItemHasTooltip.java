package com.vicious.loadmychunks.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemHasTooltip extends Item {
    public ItemHasTooltip(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable(getTooltipTranslationKey()));
    }

    public String getTooltipTranslationKey(){
        return BuiltInRegistries.ITEM.getKey(this).toLanguageKey("tooltip");
    }
}
