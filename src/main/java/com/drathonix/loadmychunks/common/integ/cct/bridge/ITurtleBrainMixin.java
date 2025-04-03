package com.drathonix.loadmychunks.common.integ.cct.bridge;

import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

// Everything of ITurtleBrainMixin will already be an ITurtleAccess
@SuppressWarnings("NonExtendableApiUsage")
public interface ITurtleBrainMixin extends ITurtleAccess {
    void lmc$removeChunkLoader();
    boolean lmc$preMove(ServerLevel oldWorld, BlockPos newPosition);
    ChunkDataModule lmc$getChunkDataModule();
    TurtleChunkLoader lmc$getOrCreateChunkLoader();
}
