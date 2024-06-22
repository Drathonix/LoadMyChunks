package com.vicious.loadmychunks.item;

import com.vicious.loadmychunks.registry.LMCContent;
import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import net.minecraft.world.item.Item;

public class LMCProperties extends Item.Properties {
    public LMCProperties(){
        if(this instanceof InjectedItemPropertiesExtension extension){
            extension.arch$tab(LMCContent.creativeTab);
        }
    }
}
