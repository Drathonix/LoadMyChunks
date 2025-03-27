package com.vicious.loadmychunks.common.system.loaders;


import com.vicious.loadmychunks.common.registry.LoaderTypeKeys;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.system.control.LoadStates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PhantomChunkLoader implements IChunkLoader{
    protected ChunkPos chunkPos;
    protected LoadStateEnum loadState = LoadStateEnum.TICKING;

    public PhantomChunkLoader(){}
    public PhantomChunkLoader(ChunkPos pos){
        this.chunkPos = pos;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putLong("l", chunkPos.toLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException {
        chunkPos = new ChunkPos(tag.getLong("chunkpos"));
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypeKeys.PHANTOM_LOADER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhantomChunkLoader that = (PhantomChunkLoader) o;
        return Objects.equals(chunkPos, that.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkPos);
    }

    public void setActiveState(LoadStates.ILoadState state){
        this.loadState = state;
    }

    @Override
    public LoadStates.ILoadState getActiveState() {
        return loadState;
    }

    @Override
    public ChunkPos getChunkPos() {
        return chunkPos;
    }
}
