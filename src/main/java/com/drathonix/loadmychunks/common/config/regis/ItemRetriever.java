package com.drathonix.loadmychunks.common.config.regis;

import com.vicious.persist.mappify.registry.Stringify;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
//? if <1.19.3
/*import net.minecraft.core.Registry;*/
//? if >1.19.2
import net.minecraft.core.registries.BuiltInRegistries;

public class ItemRetriever extends RegistryRetriever<Item> {
    static {
        Stringify.register(ItemRetriever.class, ItemRetriever::new, ItemRetriever::serializable);
    }

    public ItemRetriever(String key) {
        super(key);
    }

    public ItemRetriever(ResourceLocation key) {
        super(key);
    }

    @Override
    Item retrieve() {
        //? if <1.19.3 {
        /*return Registry.ITEM.get(location);
        *///?} else if <1.21.2 {
        /*return BuiltInRegistries.ITEM.get(location);
        *///?} else {
        return BuiltInRegistries.ITEM.get(location).get().value();
        //?}
    }
}
