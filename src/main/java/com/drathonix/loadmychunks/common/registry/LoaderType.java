package com.drathonix.loadmychunks.common.registry;

import com.drathonix.loadmychunks.common.registry.custom.LoaderTypeRegistry;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
//? if >1.16.5 {
import net.minecraft.core.Holder;
//?}
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Represents a chunk loader type to be stored in registry.
 * @param <T> the chunk loader class.
 * @since 1.2.0
 * @author Jack Andersen
 */
public class LoaderType<T extends IChunkLoader> {
    private final Supplier<@NotNull T> factory;
    //? if >1.16.5 {
    /**
     * Present for use in datapacks if ever needed.
     */
    public final Holder.Reference<LoaderType<?>> holder;
    //?}
    /**
     * Creates a new LoaderType
     * @param factory the chunk loader factory.
     */
    public LoaderType(@NotNull Supplier<T> factory){
        this.factory = factory;
        //? if >1.16.5 {
        holder = LoaderTypeRegistry.INSTANCE.createIntrusiveHolder(this);
        //?}
    }

    /**
     * Creates a chunk loader with the default state.
     * @return a chunk loader instance.
     */
    public @NotNull T create(){
        return factory.get();
    }
}
