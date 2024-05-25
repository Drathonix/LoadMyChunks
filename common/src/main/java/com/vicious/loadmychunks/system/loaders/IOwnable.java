package com.vicious.loadmychunks.system.loaders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IOwnable {
    @Nullable UUID getOwner();
    default boolean hasOwner(){
        return getOwner() != null;
    }
    void setOwner(@NotNull UUID owner);
}
