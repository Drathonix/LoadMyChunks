package com.drathonix.loadmychunks.common.system.loaders;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

/**
 * Exception thrown during {@link IChunkLoader#load(CompoundTag, ServerLevel)} that prevents it from being added to the {@link com.drathonix.loadmychunks.common.system.ChunkDataManager}
 * @since 1.2.0
 * @author Jack Andersen
 */
public class DoNotAddException extends Exception {
}
