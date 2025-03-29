package com.vicious.loadmychunks.common.system.loaders;


import com.vicious.loadmychunks.common.registry.LoaderTypeKeys;
import com.vicious.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.vicious.loadmychunks.common.system.control.ILoadState;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class PhantomChunkLoader implements IChunkLoader{
    protected ChunkPos chunkPos;
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

    @Override
    public @NotNull ChunkPos getChunkPos() {
        return chunkPos;
    }
}
