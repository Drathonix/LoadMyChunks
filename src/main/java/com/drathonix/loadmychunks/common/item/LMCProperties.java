package com.drathonix.loadmychunks.common.item;

import com.drathonix.loadmychunks.common.registry.LMCContent;
import com.drathonix.loadmychunks.common.util.ModResource;
//? if >1.19.3 {
import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
//?}
//? if >=1.21.2 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}
import net.minecraft.world.item.Item;

public class LMCProperties extends Item.Properties {
    public LMCProperties(String key){
        //? if >=1.21.2 {
        setId(ResourceKey.create(Registries.ITEM, ModResource.of(key)));
        //?}
        //? if <=1.19.3 {
        /*this.tab(LMCContent.creativeTab.get());
        *///?} else {
        if(this instanceof InjectedItemPropertiesExtension) {
            //noinspection UnstableApiUsage
            ((InjectedItemPropertiesExtension) this).arch$tab(LMCContent.creativeTab);
        }
        //?}
    }
}
