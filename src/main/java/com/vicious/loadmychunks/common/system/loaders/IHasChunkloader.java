package com.vicious.loadmychunks.common.system.loaders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IHasChunkloader {
    @Nullable IChunkLoader loadMyChunks$getChunkLoader();
    boolean loadMyChunks$extendRange(int amount);

    default boolean loadMyChunks$hasChunkLoader(){
        return loadMyChunks$getChunkLoader() != null;
    }

    default boolean supportsExtension(){
        return loadMyChunks$hasChunkLoader() && loadMyChunks$getChunkLoader().supportsExtensions();
    }

    default boolean supportsEntityTicking(){
        return loadMyChunks$hasChunkLoader() && loadMyChunks$getChunkLoader().supportsEntityTicking();
    }

    boolean enableEntityTicking();
}
