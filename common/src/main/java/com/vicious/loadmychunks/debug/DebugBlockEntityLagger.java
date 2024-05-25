package com.vicious.loadmychunks.debug;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.bridge.IDestroyable;
import com.vicious.loadmychunks.system.ChunkLoaderManager;
import com.vicious.loadmychunks.system.loaders.PlacedChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.tools.obfuscation.SuppressedBy;

import java.util.UUID;

class DebugBlockEntityLagger extends BlockEntity {

    DebugBlockEntityLagger(BlockPos blockPos, BlockState blockState) {
        super(DebugLoadMyChunks.laggerBlockEntity.get(), blockPos, blockState);
    }

    void serverTick(){
        try {
            Thread.sleep(DebugLoadMyChunks.laggerMsSleep);
        } catch (InterruptedException ignored) {}
    }
}
