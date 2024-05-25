package com.vicious.loadmychunks.system.loaders;

import com.vicious.loadmychunks.LoaderTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PhantomChunkLoader implements IChunkLoader{
    private ChunkPos position;
    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putLong("l",position.toLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        position = new ChunkPos(tag.getLong("chunkpos"));
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypes.PHANTOM_LOADER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhantomChunkLoader that = (PhantomChunkLoader) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
