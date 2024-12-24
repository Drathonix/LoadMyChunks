package com.vicious.loadmychunks.common.system.loaders;

import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.vicious.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IChunkLoader extends IChunkPositioned {
    default LoadState getLoadState() {
        return LoadState.TICKING;
    }

    default void setLoadState(LoadState state){}

    /**
     * Determines if the chunk loader instance should be saved to world data. Persistent chunk loaders will be initialized on world start.
     * This is intended for extension loaders that need to be recomputed on load
     */
    default boolean shouldPersist(){
        return true;
    }

    default boolean supportsExtensions(){
        return false;
    }

    default @Nullable ExtensionChunkLoaders getExtensionChunkLoaders() {
        return null;
    }

    default boolean hasExtensions(){
        return getExtensionChunkLoaders() != null;
    }

    default int getExtensionRange(){
        return 0;
    }

    default void setExtensionRange(int range){}

    default boolean tryExtendBy(ServerLevel serverLevel, int amount){
        int r = getExtensionRange();
        if(LMCConfig.maximumRangeExtensions < r+amount) return false;
        extend(serverLevel,amount+r);
        return true;
    }

    default void extend(ServerLevel serverLevel, int range){
        synchronized (this) {
            setExtensionRange(range);
            ExtensionChunkLoaders extensions = getExtensionChunkLoaders();
            if (range > 0) {
                if (extensions == null) {
                    extensions = new ExtensionChunkLoaders(serverLevel,this);
                    setExtensionsMap(extensions);
                }
                extensions.recompute(getExtensionClass(),range, getExtensionFactory());
            } else {
                if (extensions != null) {
                    extensions.recompute(getExtensionClass(), 0, getExtensionFactory());
                }
            }
        }
    }

    default void setExtensionsMap(ExtensionChunkLoaders extensions){}

    default ExtensionChunkLoaders.Factory<?> getExtensionFactory(){
        throw new UnsupportedOperationException("Must be implemented by child class.");
    }

    default <T extends IExtensionChunkLoader<?>> Class<T> getExtensionClass() {
        throw new UnsupportedOperationException("Must be implemented by child class.");
    }

    @NotNull CompoundTag save(@NotNull CompoundTag tag);
    void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException;

    ResourceLocation getTypeId();

    default void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime){}

    default int getExtensionCount() {
        return getExtensionChunkLoaders() != null ? getExtensionChunkLoaders().size() : 0;
    }
}