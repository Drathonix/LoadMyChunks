package com.vicious.loadmychunks.common.item;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.registry.LMCContent;
import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import net.minecraft.world.item.Item;

public class LMCProperties extends Item.Properties {
    public LMCProperties(){
        //? if <=1.16.5
        /*this.tab(LMCContent.creativeTab.get());*/
        //? if >1.16.5 {
        if(this instanceof InjectedItemPropertiesExtension) {
            //noinspection UnstableApiUsage
            ((InjectedItemPropertiesExtension) this).arch$tab(LMCContent.creativeTab.get());
        }
        //?}
    }
}
