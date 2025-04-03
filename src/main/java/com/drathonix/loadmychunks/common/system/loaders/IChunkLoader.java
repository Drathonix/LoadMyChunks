package com.drathonix.loadmychunks.common.system.loaders;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.drathonix.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Any object that implements this interface can be stored in the ChunkDataManager and can load chunks.
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface IChunkLoader extends IChunkPositioned {
    /**
     * Each loader should have an "active" and a "default" state. The active state is what is currently applied to the chunk.
     * Override this method for control over the loader's active state.
     * @return the loader's current state
     */
    default ILoadState getActiveState() {
        return getDefaultState();
    }

    /**
     * When a loader reactivates it uses this state as its active state. Override this to change.
     * @return the current default state.
     */
    default ILoadState getDefaultState(){
        return LoadStateRegistry.TICKING;
    }

    /**
     * Sets the loader's default state.
     * @param state the new default state.
     * @return the previous state.
     */
    default ILoadState setDefaultState(ILoadState state){
        throw new IllegalStateException("setDefaultState not implemented for this chunk loader");
    }

    /**
     * Allows changing the chunk loader's loader state.
     * @param state the state to change to
     */
    default void setActiveState(ILoadState state){}

    /**
     * Determines if the chunk loader instance should be saved to world data. Persistent chunk loaders will be initialized on world start.
     * This is intended for extension loaders that need to be recomputed on load
     * @return whether the chunkloader should be saved to world data.
     */
    default boolean shouldPersist(){
        return true;
    }

    /**
     * This will be checked when a player tries extending a chunk loader. Override to enable extensions.
     * @return whether extensions are supported.
     */
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

    @ApiStatus.NonExtendable
    default boolean tryExtendBy(ServerLevel serverLevel, int amount){
        int r = getExtensionRange();
        if(LMCConfig.maximumRangeExtensions < r+amount) return false;
        extend(serverLevel,amount+r);
        return true;
    }

    @ApiStatus.NonExtendable
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

    default boolean shouldConsumeItems(){
        return LMCConfig.cost.enabled;
    }

    default @NotNull BlockPos getItemSource(){
        return new BlockPos(0,0,0);
    }

    default long getActivityEnd(){
        return -1;
    }
    default void setActivityEnd(long l){}

    @ApiStatus.NonExtendable
    default void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime) {
        if(!getActiveState().shouldLoad()){
            return;
        }
        long activityEnd = getActivityEnd();
        long timeRemaining = activityEnd-gameTime;
        long duration = LMCConfig.cost.getDurationFor(getActiveState());
        if(duration/10L >= timeRemaining){
            if(LMCConfig.consumeFuel(level,getItemSource())){
                activityEnd=gameTime+Math.max(0,timeRemaining)+duration*20;
                setActivityEnd(activityEnd);
                chunkDataModule.updateCheckTime(activityEnd-duration/10L);
            }
        }
        timeRemaining = activityEnd-gameTime;
        if(timeRemaining <= 0){
            setActivityEnd(-1);
        }
    }

    @ApiStatus.NonExtendable
    default int getExtensionCount() {
        return getExtensionChunkLoaders() != null ? getExtensionChunkLoaders().size() : 0;
    }

    default boolean supportsEntityTicking(){
        return false;
    }

    /**
     * Enables entity ticking if not already enabled.
     * @return true if the state changed
     */
    @ApiStatus.NonExtendable
    default boolean enableEntityTicking(ServerLevel level) {
        boolean changed = setDefaultState(LoadStateRegistry.ENTITY_TICKING) != LoadStateRegistry.ENTITY_TICKING;
        if(changed){
            Optional.ofNullable(getExtensionChunkLoaders()).ifPresent(ExtensionChunkLoaders::requestUpdates);
            ChunkDataManager.requestUpdate(level,getChunkPos());
        }
        return changed;
    }
}