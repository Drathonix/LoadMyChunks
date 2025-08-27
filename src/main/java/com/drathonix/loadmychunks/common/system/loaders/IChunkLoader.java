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
import org.jetbrains.annotations.Range;

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

    /**
     * Gets the extension map. Only applicable if {@link IChunkLoader#supportsExtensions()} is true
     * @return null or the extension chunk loader map.
     */
    default @Nullable ExtensionChunkLoaders getExtensionChunkLoaders() {
        return null;
    }

    /**
     * Checks if the chunk loader has extensions.
     * @return whether the extension map is present.
     */
    default boolean hasExtensions(){
        return getExtensionChunkLoaders() != null;
    }

    /**
     * Gets the extension range. Only applicable if {@link IChunkLoader#supportsExtensions()} is true
     * @return the extension range.
     */
    default int getExtensionRange(){
        return 0;
    }

    /**
     * Sets the extension range. Only applicable if {@link IChunkLoader#supportsExtensions()}
     * @param range the range to change to.
     */
    default void setExtensionRange(int range){}

    /**
     * Tries to extend a chunk loader if it has not exceeded the maximum range.
     * @param serverLevel the chunk loader's level.
     * @param amount the amount to extend by.
     * @return false if the chunk loader cannot be extended anymore.
     */
    @ApiStatus.NonExtendable
    default boolean tryExtendBy(ServerLevel serverLevel, int amount){
        int r = getExtensionRange();
        if(LMCConfig.maximumRangeExtensions < r+amount) return false;
        extend(serverLevel,amount+r);
        return true;
    }

    /**
     * Extends the range of a chunk loader. Only applicable if {@link IChunkLoader#supportsExtensions()} is true.
     * @param serverLevel the level of the chunk loader.
     * @param range the new range of extension.
     */
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

    /**
     * Sets the extensions map. Only applicable if {@link IChunkLoader#supportsExtensions()} is true.
     * @param extensions the extensions instance.
     */
    default void setExtensionsMap(ExtensionChunkLoaders extensions){
        throw new UnsupportedOperationException("Must be implemented by child class.");
    }

    /**
     * Gets the extension chunk loader storage factory, only applicable if {@link IChunkLoader#supportsExtensions()} is true.
     * @return a factory.
     */
    default ExtensionChunkLoaders.Factory<?> getExtensionFactory(){
        throw new UnsupportedOperationException("Must be implemented by child class.");
    }

    /**
     * Gets the extension type class, only applicable if {@link IChunkLoader#supportsExtensions()} is true.
     * @return the extension type class.
     * @param <T> the extension type.
     */
    default <T extends IExtensionChunkLoader<?>> Class<T> getExtensionClass() {
        throw new UnsupportedOperationException("Must be implemented by child class.");
    }

    /**
     * Writes the chunk loader state to a compound tag.
     * @param tag the tag to save to.
     * @return the modified compound tag.
     */
    @NotNull CompoundTag save(@NotNull CompoundTag tag);

    /**
     * Loads the state from a compound tag.
     * @param tag the state tag.
     * @param level the level of the chunk loader instance.
     * @throws DoNotAddException if the chunk loader is invalid and should not be added to the {@link ChunkDataModule}
     */
    void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException;

    /**
     * Executed after the chunk loader has had its NBT loaded and added to the chunk loader manager.
     * @param level the level where loading has occurred.
     * @throws DoNotAddException if the chunk loader is invalid and should not be added to the {@link ChunkDataModule}
     * @since 1.2.0
     */
    default void postLoad(ServerLevel level) throws DoNotAddException {}

    /**
     * Gets the id of this chunk loader for reference in {@link com.drathonix.loadmychunks.common.registry.custom.LoaderTypeRegistry}
     * @return the registry id.
     */
    ResourceLocation getTypeId();

    /**
     * Controls if the chunk loader should consume items.
     * @return whether to consume items to increase loading time.
     */
    default boolean shouldConsumeItems(){
        return LMCConfig.cost.enabled;
    }

    /**
     * Must be implemented to provide support for {@link LMCConfig.Cost}
     * @return the position of an {@link net.minecraft.world.level.block.entity.BlockEntity} {@link net.minecraft.world.Container} to consume items from.
     */
    @NotNull BlockPos getItemSource();

    /**
     * Gets the end of loading activity. Only used if {@link LMCConfig.Cost#enabled} is true.
     * @return the end time of chunk ticking.
     */
    default long getActivityEnd(){
        return -1;
    }

    /**
     * Sets the end of loading activity. Only used if {@link LMCConfig.Cost#enabled} is true.
     * @param l the loading end time.
     */
    default void setActivityEnd(long l){}

    /**
     * Only applied if {@link LMCConfig.Cost#enabled} is true. Consumes items if 1/10 or less of the duration is remaining and grants more loading time.
     * @param level the level.
     * @param chunkDataModule the chunk loader's CDM.
     * @param gameTime the current game tick.
     */
    @ApiStatus.NonExtendable
    default void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime) {
        if(!getDefaultState().shouldLoad()){
            return;
        }
        long activityEnd = getActivityEnd();
        long timeRemaining = activityEnd-gameTime;
        long duration = LMCConfig.cost.getDurationFor(getDefaultState());
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

    /**
     * Gets the number of extension upgrades applied to the chunk loader.
     * @return some positive integer.
     */
    @ApiStatus.NonExtendable
    @Range(from = 0, to = Integer.MAX_VALUE)
    default int getExtensionCount() {
        return getExtensionChunkLoaders() != null ? getExtensionChunkLoaders().size() : 0;
    }

    /**
     * When true the chunk loader can be upgraded to support entity ticking.
     * @return whether entity ticking is supported.
     */
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