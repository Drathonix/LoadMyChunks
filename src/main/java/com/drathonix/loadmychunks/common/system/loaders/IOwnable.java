package com.drathonix.loadmychunks.common.system.loaders;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents an object with a player UUID representing the owner of the object. The object may not be owned.
 * @author Jack Andersen
 * @since 1.2.0
 */
public interface IOwnable {
    /**
     * Gets the object owner or null if there is no owner.
     * @return null or a player UUID.
     */
    @Nullable UUID getOwner();

    /**
     * Effectively a null check.
     * @return whether the owner object is present.
     */
    @ApiStatus.NonExtendable
    default boolean hasOwner(){
        return getOwner() != null;
    }

    /**
     * Sets the object's owner UUID.
     * @param owner the owner UUID
     */
    void setOwner(@NotNull UUID owner);

    /**
     * Checks if the owner has exceeded the chunk limit. The null owner counts as the global owner.
     * @return whether the chunk limit has been exceeded.
     */
    @ApiStatus.NonExtendable
    default boolean hasExceededChunkLimit(){
        return ChunkDataManager.hasExceededOwnershipCap(getOwner());
    }
}
