package com.drathonix.loadmychunks.common.item;

import com.drathonix.loadmychunks.common.util.Message;
//? if >1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockItemHasTooltip extends BlockItem {
    private final int nLines;

    public BlockItemHasTooltip(Block block, Properties properties, int nLines) {
        super(block, properties);
        this.nLines = nLines;
    }

    //? if <=1.20.5 {
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        for (int i = 0; i < nLines; i++) {
            list.add(Message.translatable(getTooltipTranslationKey(i)));
        }

    }
    //?} else {
    /*@Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        for (int i = 0; i < nLines; i++) {
            list.add(Message.translatable(getTooltipTranslationKey(i)));
        }
    }
    *///?}

    public String getTooltipTranslationKey(int k){
        //? if <=1.19.3 {
        /*return "tooltip." + Registry.ITEM.getKey(this).toString().replace(":",".") + "." + k;
        *///?} else {
        return "tooltip." + BuiltInRegistries.ITEM.getKey(this).toString().replace(":",".") + "." + k;
        //?}
    }
}
