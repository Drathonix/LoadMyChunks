package com.vicious.loadmychunks.common.system.loaders;

import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for objects that have a chunk position.
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface IChunkPositioned {
    /**
     * Gets the chunk pos for the object.
     * @return a nonnull chunkpos
     */
    @NotNull
    ChunkPos getChunkPos();
}
