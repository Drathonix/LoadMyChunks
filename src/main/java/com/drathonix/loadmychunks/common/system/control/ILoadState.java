package com.drathonix.loadmychunks.common.system.control;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created after
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface ILoadState {
    /**
     * Controls if this state can force a chunk to remain loaded and tick block entities.
     *
     * @return FORCED if unrestricted by LMC chunk rules, FORCED_MANAGED if follows chunk rules, DISABLED if not forced at all.
     */
    LoaderPower blockEntityTickingPower();

    /**
     * Controls if this state can force a chunk tick entities and do random ticks.
     *
     * @return FORCED if unrestricted by LMC chunk rules, FORCED_MANAGED if follows chunk rules, DISABLED if not forced at all.
     */
    LoaderPower entityForcingPower();

    int id();

    /**
     * @param state some other load state.
     * @return If this state will override the other state.
     */
    default boolean overrides(ILoadState state) {
        return blockEntityTickingPower().ordinal() >= state.blockEntityTickingPower().ordinal() && entityForcingPower().ordinal() >= state.entityForcingPower().ordinal();
    }

    default boolean shouldLoad() {
        return blockEntityTickingPower() == LoaderPower.FORCED_MANAGED || blockEntityTickingPower() == LoaderPower.FORCED;
    }

    default boolean shouldForceEntities() {
        return entityForcingPower() == LoaderPower.FORCED_MANAGED || entityForcingPower() == LoaderPower.FORCED;
    }

    default ILoadState getSuperiorLoadState(ILoadState loadState) {
        if (loadState.overrides(this)) {
            return loadState;
        }
        return this;
    }

    default boolean permanent() {
        return blockEntityTickingPower() == LoaderPower.FORCED;
    }

    default void apply(@NotNull ServerLevel level, long pos, @Nullable ILoadState previous) {
        apply(level, new ChunkPos(pos), previous);
    }

    default void apply(@NotNull ServerLevel level, @NotNull BlockPos pos, @Nullable ILoadState previous) {
        apply(level, new ChunkPos(pos), previous);
    }

    default void apply(@NotNull ServerLevel level, @NotNull ChunkPos pos, @Nullable ILoadState previous) {
        if(previous == null) previous = LoadStateRegistry.DISABLED;
        if (shouldLoad()) {
            if (!shouldForceEntities()) {
                LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Forceloading Chunk at: (" + pos.x + "," + pos.z + ") with level " + blockEntityTickingPower().name());
            } else {
                LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Entity Ticking Chunk at: (" + pos.x + "," + pos.z + ") with level " + entityForcingPower().name());
            }
            ChunkForcer.forceChunk(level, pos, shouldForceEntities());
        } else {
            LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Unforceloading Chunk at: (" + pos.x + "," + pos.z + ")");
            ChunkForcer.unforceChunk(level, pos, previous.shouldForceEntities());
        }
    }

    default void putCompound(String key, CompoundTag tag) {
        tag.putInt(key, id());
    }
}
