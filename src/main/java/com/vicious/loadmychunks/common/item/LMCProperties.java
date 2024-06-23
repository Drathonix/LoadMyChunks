package com.vicious.loadmychunks.common.item;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.registry.LMCContent;
//? if >1.19.4
/*import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;*/
import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import net.minecraft.world.item.Item;

public class LMCProperties extends Item.Properties {
    public LMCProperties(){
        //? if <=1.19.3
        /*this.tab(LMCContent.creativeTab.get());*/
        //? if >1.19.3 {
        if(this instanceof InjectedItemPropertiesExtension) {
            //noinspection UnstableApiUsage
            ((InjectedItemPropertiesExtension) this).arch$tab(LMCContent.creativeTab);
        }
        //?}
    }
}
