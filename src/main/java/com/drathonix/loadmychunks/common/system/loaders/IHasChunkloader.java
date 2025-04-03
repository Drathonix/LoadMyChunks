package com.drathonix.loadmychunks.common.system.loaders;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for objects that have a chunk loader instance.
 * @author Jack Andersen
 * @since 1.2.0
 */
public interface IHasChunkloader {
    /**
     * Gets the chunk loader instance if present.
     * @return a nullable chunk loader instance.
     */
    @Nullable IChunkLoader loadMyChunks$getChunkLoader();

    /**
     * Executes an arbitrary void function if the chunk loader instance is not null.
     * @param consumer the void function.
     */
    @ApiStatus.NonExtendable
    default void ifPresent(@NotNull Consumer<IChunkLoader> consumer){
        IChunkLoader loader = loadMyChunks$getChunkLoader();
        if(loader != null){
            consumer.accept(loader);
        }
    }

    /**
     * Executes an arbitrary function if the chunk loader instance is not null.
     * @param fn the function.
     * @return null or some T.
     * @param <T> the return type.
     */
    @ApiStatus.NonExtendable
    default <T> @Nullable T map(@NotNull Function<IChunkLoader,T> fn){
        IChunkLoader loader = loadMyChunks$getChunkLoader();
        if(loader != null){
            return fn.apply(loader);
        }
        return null;
    }

    /**
     * Executes an arbitrary void function if the chunk loader instance is not null given that the obj provided is an implementer of this interface.
     * @param obj the possible IHasChunkLoader
     * @param consumer the void function.
     */
    static void ifPresent(@Nullable Object obj, @NotNull Consumer<IChunkLoader> consumer){
        if(obj instanceof IHasChunkloader){
            ((IHasChunkloader)obj).ifPresent(consumer);
        }
    }

    /**
     * Executes an arbitrary function if the chunk loader instance is not null given that the obj provided is an implementer of this interface.
     * @param obj the possible IHasChunkLoader.
     * @param fn some function
     * @return null or some T.
     * @param <T> the return type.
     */
    static <T> @Nullable T map(@Nullable Object obj, @NotNull Function<IChunkLoader,T> fn){
        if(obj instanceof IHasChunkloader){
            return ((IHasChunkloader)obj).map(fn);
        }
        return null;
    }

    /**
     * Useful for null safety and functional programming if the provided functional methods are not enough.
     * @return an empty or full optional of IChunkLoader.
     */
    @ApiStatus.NonExtendable
    default Optional<IChunkLoader> optional(){
        return Optional.ofNullable(loadMyChunks$getChunkLoader());
    }
}
