package com.drathonix.loadmychunks.common.config.regis;

import com.vicious.persist.annotations.Save;
//? if <1.19.3
/*import net.minecraft.core.Registry;*/
//? if >1.19.2
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class ItemStackRetriever {
    @Save
    public ItemRetriever item;
    @Save
    public int size;

    public ItemStackRetriever(){}

    public ItemStackRetriever(ItemStack stack) {
        //? if >1.19.2
        this.item = new ItemRetriever(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        //? if <1.19.3
        /*this.item = new ItemRetriever(Registry.ITEM.getKey(stack.getItem()));*/
        this.size=stack.getCount();
    }

    public ItemStack get() {
        return new ItemStack(item.get(),size);
    }

    public boolean is(ItemStack stack) {
        return stack.getItem() == item.retrieve();
    }
}
