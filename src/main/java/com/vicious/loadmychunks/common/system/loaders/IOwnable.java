package com.vicious.loadmychunks.common.system.loaders;

import com.vicious.loadmychunks.common.system.ChunkDataManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IOwnable {
    @Nullable UUID getOwner();
    default boolean hasOwner(){
        return getOwner() != null;
    }
    void setOwner(@NotNull UUID owner);
    default boolean hasExceededChunkLimit(){
        return ChunkDataManager.hasExceededOwnershipCap(getOwner());
    }
}
