package com.vicious.loadmychunks.common.registry;

import com.vicious.loadmychunks.common.LoadMyChunks;
//? if >1.16.5 {
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
//?}
//? if >1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//?}
//? if <=1.16.5 {
/*import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.Registries;
*///?}
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//Controls Registry Load order.
public class LMCRegistrar<T> {
    //? if <=1.19.3 {
    /*public static final LMCRegistrar<Item> ITEM = new LMCRegistrar<>(Registry.ITEM_REGISTRY);
    public static final LMCRegistrar<Block> BLOCK = new LMCRegistrar<>(Registry.BLOCK_REGISTRY);
    public static final LMCRegistrar<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new LMCRegistrar<>(Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    *///?}
    //? if >1.19.3 {
    public static final LMCRegistrar<Item> ITEM = new LMCRegistrar<>(Registries.ITEM);
    public static final LMCRegistrar<Block> BLOCK = new LMCRegistrar<>(Registries.BLOCK);
    public static final LMCRegistrar<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new LMCRegistrar<>(Registries.BLOCK_ENTITY_TYPE);
    //?}
    private final DeferredRegister<T> register;
    private final List<Consumer<DeferredRegister<T>>> actions = new ArrayList<>();
    private final ResourceKey<Registry<T>> key;

    public LMCRegistrar(ResourceKey<Registry<T>> key){
        register = DeferredRegister.create(LoadMyChunks.MOD_ID,key);
        this.key=key;
    }

    public static void init() {
        BLOCK.run();
        ITEM.run();
        BLOCK_ENTITY_TYPE.run();
    }

    public void queue(Consumer<DeferredRegister<T>> action){
        actions.add(action);
    }

    public void run(){
        for (Consumer<DeferredRegister<T>> action : actions) {
            action.accept(register);
        }
        register.register();
    }

    //Doing things this way to be more version universal
    @SuppressWarnings("unchecked")
    public T get(ResourceLocation key) {
        //? if <=1.19.3
        /*return (T)Registry.REGISTRY.get(this.key.location()).get(key);*/
        //? if >1.19.3 && <1.21.2
        /*return (T) BuiltInRegistries.REGISTRY.get(this.key.location()).get(key);*/
        //? if >1.21.1
        return (T) BuiltInRegistries.REGISTRY.get(this.key.location()).get().value().get(key).get().value();
    }
}