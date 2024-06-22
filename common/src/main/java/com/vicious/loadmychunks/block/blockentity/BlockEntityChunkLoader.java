package com.vicious.loadmychunks.block.blockentity;

import com.vicious.loadmychunks.bridge.IDestroyable;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.loaders.PlacedChunkLoader;
import com.vicious.loadmychunks.registry.LMCContent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class BlockEntityChunkLoader extends BlockEntity implements IDestroyable {
    private PlacedChunkLoader chunkLoader;
    private UUID owner;

    public BlockEntityChunkLoader(BlockPos blockPos, BlockState blockState) {
        super(LMCContent.chunkLoaderBlockEntity.get(), blockPos, blockState);
    }

    public PlacedChunkLoader getChunkLoader() {
        return chunkLoader;
    }
    @Override
    public void destroy() {
        if(level instanceof ServerLevel sl) {
            ChunkDataManager.removeChunkLoader(sl,getBlockPos(),chunkLoader);
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if(level instanceof ServerLevel sl && chunkLoader == null) {
            chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent(sl,getBlockPos(),PlacedChunkLoader.class, loader-> loader.getPosition().equals(getBlockPos()),()-> new PlacedChunkLoader(getBlockPos()));
            if(owner != null){
                chunkLoader.setOwner(owner);
            }
        }
    }

    public void setOwner(UUID uuid) {
        this.owner=uuid;
        if(chunkLoader != null){
            this.chunkLoader.setOwner(uuid);
        }
    }
}


