package com.vicious.loadmychunks.common.system.loaders;

import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.vicious.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.PlacedExtensionChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class PlacedChunkLoader implements IChunkLoader,IOwnable {
    @Nullable protected ExtensionChunkLoaders extensions = null;
    protected int extensionRange = 0;
    protected UUID owner;
    protected BlockPos position;
    protected LoadState loadState = LoadState.TICKING;

    public PlacedChunkLoader(){}

    public PlacedChunkLoader(BlockPos pos){
        this.position = pos;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        if(hasOwner()) {
            tag.putUUID("owner", owner);
        }
        if(hasExtensions()){
            tag.putInt("extensions",extensionRange);
        }
        tag.putInt("state",loadState.ordinal());
        tag.putLong("pos",position.asLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) {
        if(tag.contains("owner")){
            owner = tag.getUUID("owner");
        }
        if(tag.contains("state")){
            loadState = LoadState.values()[tag.getInt("state")];
        }
        if(tag.contains("extensions")){
            extensionRange = tag.getInt("extensions");
            extensions = new ExtensionChunkLoaders(level,this);
            extensions.recompute(PlacedExtensionChunkLoader.class,extensionRange,this::createExtension);
        }
        position = BlockPos.of(tag.getLong("pos"));
    }

    public boolean hasExtensions(){
        return extensions != null;
    }

    public int getExtensionRange(){
        return extensionRange;
    }

    public PlacedExtensionChunkLoader createExtension(ChunkPos position) {
        return new PlacedExtensionChunkLoader(position,this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IExtensionChunkLoader<?>> Class<T> getExtensionClass() {
        return (Class<T>) PlacedExtensionChunkLoader.class;
    }

    @Override
    public boolean supportsExtensions() {
        return true;
    }

    @Override
    public ExtensionChunkLoaders.Factory<?> getExtensionFactory() {
        return this::createExtension;
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

    @Override
    public ChunkPos getChunkPos() {
        return new ChunkPos(getPosition());
    }
}
