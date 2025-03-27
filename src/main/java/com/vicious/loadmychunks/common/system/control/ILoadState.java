package com.vicious.loadmychunks.common.system.control;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.system.ThreadSafetyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

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

    default void apply(ServerLevel level, long pos) {
        apply(level, new ChunkPos(pos));
    }

    default void apply(ServerLevel level, BlockPos pos) {
        apply(level, new ChunkPos(pos));
    }

    //TODO: make sure this is thread safe.
    default void apply(ServerLevel level, ChunkPos pos) {
        if (shouldLoad()) {
            if (!shouldForceEntities()) {
                LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Forceloading Chunk at: (" + pos.x + "," + pos.z + ") with level " + blockEntityTickingPower().name());
                ThreadSafetyHelper.forceChunk(level, pos);
            } else {
                LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Entity Ticking Chunk at: (" + pos.x + "," + pos.z + ") with level " + entityForcingPower().name());
                ThreadSafetyHelper.forceChunk(level, pos, true);
            }
        } else {
            LoadMyChunks.logger.log(LoadMyChunks.debugLevel, "Unforceloading Chunk at: (" + pos.x + "," + pos.z + ")");
            ThreadSafetyHelper.unforceChunk(level, pos);
        }
    }

    default void putCompound(String key, CompoundTag tag) {
        tag.putInt(key, id());
    }
}
