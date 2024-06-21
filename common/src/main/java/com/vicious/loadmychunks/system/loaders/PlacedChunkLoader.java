package com.vicious.loadmychunks.system.loaders;

import com.vicious.loadmychunks.LoaderTypes;
import com.vicious.loadmychunks.system.control.LoadState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class PlacedChunkLoader implements IChunkLoader,IOwnable{
    private UUID owner;
    private BlockPos position;
    private LoadState loadState = LoadState.TICKING;

    public PlacedChunkLoader(){}

    public PlacedChunkLoader(BlockPos pos){
        this.position = pos;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        if(hasOwner()) {
            tag.putUUID("owner", owner);
        }
        tag.putInt("state",loadState.ordinal());
        tag.putLong("pos",position.asLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        if(tag.contains("owner")){
            owner = tag.getUUID("owner");
        }
        if(tag.contains("state")){
            loadState = LoadState.values()[tag.getInt("state")];
        }
        position = BlockPos.of(tag.getLong("pos"));
    }

    @Override
    public @Nullable UUID getOwner() {
        return owner;
    }

    @Override
    public LoadState getLoadState() {
        return loadState;
    }

    @Override
    public void setLoadState(LoadState state) {
        this.loadState =state;
    }

    @Override
    public void setOwner(@NotNull UUID owner) {
        this.owner=owner;
    }

    public @NotNull BlockPos getPosition() {
        return position;
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypes.PLACED_LOADER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacedChunkLoader that = (PlacedChunkLoader) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
