package com.vicious.loadmychunks.system;

import com.vicious.loadmychunks.LoaderTypes;
import com.vicious.loadmychunks.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.system.loaders.PhantomChunkLoader;
import com.vicious.loadmychunks.system.loaders.PlacedChunkLoader;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Needed to support using multiple different types of IChunkLoader.
 */
public class LoaderTypeRegistry {
    private static final Map<ResourceLocation, Supplier<? extends IChunkLoader>> types = new HashMap<>();

    public static void register(ResourceLocation id, Supplier<? extends IChunkLoader> factory){
        types.put(id,factory);
    }

    public static Supplier<? extends IChunkLoader> getFactory(ResourceLocation id){
        return types.get(id);
    }

    static {
        register(LoaderTypes.PLACED_LOADER, PlacedChunkLoader::new);
        register(LoaderTypes.PHANTOM_LOADER, PhantomChunkLoader::new);
    }
}
