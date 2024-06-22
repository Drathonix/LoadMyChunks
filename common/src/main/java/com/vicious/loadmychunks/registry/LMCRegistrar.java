package com.vicious.loadmychunks.registry;

import com.vicious.loadmychunks.LoadMyChunks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//Controls Registry Load order.
public class LMCRegistrar<T> {
    public static final LMCRegistrar<Item> ITEM = new LMCRegistrar<>(Registries.ITEM);
    public static final LMCRegistrar<Block> BLOCK = new LMCRegistrar<>(Registries.BLOCK);
    public static final LMCRegistrar<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new LMCRegistrar<>(Registries.BLOCK_ENTITY_TYPE);

    private final DeferredRegister<T> register;
    private final List<Consumer<DeferredRegister<T>>> actions = new ArrayList<>();

    public LMCRegistrar(ResourceKey<Registry<T>> key){
        register = DeferredRegister.create(LoadMyChunks.MOD_ID,key);
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

    public Registrar<T> getRegistrar() {
        return register.getRegistrar();
    }
}
