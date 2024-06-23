package com.vicious.loadmychunks.common.block.blockentity;

import com.vicious.loadmychunks.common.bridge.IDestroyable;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class BlockEntityChunkLoader extends BEBase implements IDestroyable {
    private PlacedChunkLoader chunkLoader;
    private UUID owner;

    public BlockEntityChunkLoader() {
        super(LMCContent.chunkLoaderBlockEntity.get());
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
    public void validate(Level level) {
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
