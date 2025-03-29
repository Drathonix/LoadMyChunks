package com.vicious.loadmychunks.common.item;

import net.minecraft.network.chat.Component;
//? if <1.18.3
/*import net.minecraft.network.chat.TranslatableComponent;*/
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemChunkLoader extends BlockItemHasTooltip {
    public ItemChunkLoader(Block block, Properties properties) {
        super(block, properties, 2);
    }

    @Override
    public String getTooltipTranslationKey(int k) {
        return "tooltip.loadmychunks.chunk_loader." + k;
    }
}
