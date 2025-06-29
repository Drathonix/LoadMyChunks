package com.drathonix.loadmychunks.common.item;

//? if <1.18.3
/*import net.minecraft.network.chat.TranslatableComponent;*/
import net.minecraft.world.level.block.Block;

public class ItemChunkLoader extends BlockItemHasTooltip {
    public ItemChunkLoader(Block block, Properties properties) {
        super(block, properties, 2);
    }

    @Override
    public String getTooltipTranslationKey(int k) {
        return "tooltip.loadmychunks.chunk_loader." + k;
    }
}
