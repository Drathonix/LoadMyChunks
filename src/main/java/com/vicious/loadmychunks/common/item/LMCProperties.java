package com.vicious.loadmychunks.common.item;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.registry.LMCContent;
import net.minecraft.world.item.Item;

public class LMCProperties extends Item.Properties {
    public LMCProperties(){
        this.tab(LMCContent.creativeTab.get());
    }
}
