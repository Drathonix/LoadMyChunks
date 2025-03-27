package com.vicious.loadmychunks.common.registry;

import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Represents a chunk loader type to be stored in registry.
 * @param <T> the chunk loader class.
 */
public class LoaderType<T extends IChunkLoader> {
    private final Supplier<T> factory;

    /**
     * Creates a new LoaderType
     * @param factory the chunk loader factory.
     */
    public LoaderType(@NotNull Supplier<T> factory){
        this.factory = factory;
    }

    /**
     * Creates a chunk loader with the default state.
     * @return a chunk loader instance.
     */
    public @NotNull T create(){
        return factory.get();
    }
}
