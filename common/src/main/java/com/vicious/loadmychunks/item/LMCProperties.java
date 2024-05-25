package com.vicious.loadmychunks.item;

import com.vicious.loadmychunks.LoadMyChunks;
import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import dev.architectury.impl.ItemPropertiesExtensionImpl;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class LMCProperties extends Item.Properties {
    public LMCProperties(){
        if(this instanceof InjectedItemPropertiesExtension extension){
            extension.arch$tab(LoadMyChunks.creativeTab);
        }
    }
}
