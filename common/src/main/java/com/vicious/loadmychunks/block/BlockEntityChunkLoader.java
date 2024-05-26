package com.vicious.loadmychunks.block;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.bridge.IDestroyable;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.loaders.PlacedChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class BlockEntityChunkLoader extends BlockEntity implements IDestroyable {
    private PlacedChunkLoader chunkLoader;
    private UUID owner;

    public BlockEntityChunkLoader() {
        super(LoadMyChunks.chunkLoaderBlockEntity.get());
    }

    public PlacedChunkLoader getChunkLoader() {
        return chunkLoader;
    }
    @Override
    public void destroy() {
        if(level instanceof ServerLevel) {
            ChunkDataManager.removeChunkLoader((ServerLevel) level,getBlockPos(),chunkLoader);
        }
    }

    @Override
    public void setLevelAndPosition(Level level, BlockPos blockPos) {
        super.setLevelAndPosition(level, blockPos);
        if(level instanceof ServerLevel && chunkLoader == null) {
            chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent((ServerLevel)level,getBlockPos(),PlacedChunkLoader.class, loader-> loader.getPosition().equals(getBlockPos()),()-> new PlacedChunkLoader(getBlockPos()));
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
