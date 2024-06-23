package com.vicious.loadmychunks.common.item;

import net.minecraft.core.Registry;
//? if >1.16.5
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.network.chat.Component;
//? if <=1.16.5
/*import net.minecraft.network.chat.TranslatableComponent;*/
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemHasTooltip extends Item {
    public ItemHasTooltip(Properties properties) {
        super(properties);
    }

    //? if <=1.20.5 {
    /*@Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        //? if <=1.16.5
        /^list.add(new TranslatableComponent(getTooltipTranslationKey()));^/
        //? if >1.16.5
        list.add(Component.translatable(getTooltipTranslationKey()));
    }
    *///?}

    //? if >1.20.5 {
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable(getTooltipTranslationKey()));
    }
    //?}

    public String getTooltipTranslationKey(){
        //? if <=1.16.5
        /*return "tooltip." + Registry.ITEM.getKey(this).toString().replace(":",".");*/
        //> if >1.16.5
        return "tooltip." + BuiltInRegistries.ITEM.getKey(this).toString().replace(":",".");
    }
}
