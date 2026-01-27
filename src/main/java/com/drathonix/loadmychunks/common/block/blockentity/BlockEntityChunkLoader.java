package com.drathonix.loadmychunks.common.block.blockentity;

import com.drathonix.loadmychunks.common.bridge.IDestroyable;
import com.drathonix.loadmychunks.common.registry.LMCContent;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class BlockEntityChunkLoader extends BEBase implements IDestroyable, IHasChunkloader {
    private PlacedChunkLoader chunkLoader;
    private UUID owner = Util.NIL_UUID;

    //? if <=1.16.5 {
    public BlockEntityChunkLoader() {
        super(LMCContent.chunkLoaderBlockEntity.get());
    }
    //?} else {
    /*public BlockEntityChunkLoader(BlockPos blockPos, BlockState blockState) {
        super(LMCContent.chunkLoaderBlockEntity.get(), blockPos, blockState);
    }
    *///?}

    @Override
    public PlacedChunkLoader loadMyChunks$getChunkLoader() {
        return chunkLoader;
    }

    @Override
    protected void read(CompoundTag tag) {
        super.read(tag);
        if(owner != null) {
            tag.putUUID("owner", owner);
        }
    }

    @Override
    protected void write(CompoundTag tag) {
        super.write(tag);
        if(tag.contains("owner")) {
            setOwner(tag.getUUID("owner"));
        }
    }

    @Override
    public void loadMyChunks$destroy() {
        if(level instanceof ServerLevel) {
            ChunkDataManager.removeChunkLoader((ServerLevel) level,getBlockPos(),chunkLoader);
        }
    }

    @Override
    public void validate(Level level) {
        MultiversioningHelper.serverLevel(level,sl->{
            if(chunkLoader == null) {
                chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent(sl,getBlockPos(),PlacedChunkLoader.class, loader-> loader.getPosition().equals(getBlockPos()),()-> new PlacedChunkLoader(getBlockPos(),owner));
            }
        });
    }

    public void setOwner(UUID uuid) {
        UUID prev = this.owner;
        this.owner=uuid == null ? Util.NIL_UUID : uuid;
        MultiversioningHelper.serverLevel(level,sl->{
            if(chunkLoader != null){
                this.chunkLoader.setOwner(uuid);
                ChunkDataManager.markChunkNotOwnedBy(sl,chunkLoader.getChunkPos().toLong(),prev);
                ChunkDataManager.markChunkOwnedBy(sl,chunkLoader.getChunkPos().toLong(), uuid);
            }
        });
    }
}
