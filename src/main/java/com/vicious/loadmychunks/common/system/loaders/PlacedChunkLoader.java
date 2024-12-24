package com.vicious.loadmychunks.common.system.loaders;

import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.control.LoadState;
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
    @Nullable protected UUID owner;
    protected BlockPos position;
    protected LoadState loadState = LoadState.TICKING;
    protected long activityEnd = -1;

    public PlacedChunkLoader(){}

    public PlacedChunkLoader(BlockPos pos){
        this.position = pos;
    }
    public PlacedChunkLoader(BlockPos pos, long activityEnd){
        this.position = pos;
        this.activityEnd=activityEnd;
    }
    public PlacedChunkLoader(BlockPos pos, @Nullable UUID owner){
        this.position = pos;
        this.owner = owner;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        if(hasOwner()) {
            tag.putUUID("owner", owner);
        }
        if(hasExtensions()){
            tag.putInt("extensions",extensionRange);
        }
        tag.putLong("duration", activityEnd);
        tag.putInt("state",loadState.ordinal());
        tag.putLong("pos",position.asLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) {
        if(tag.contains("owner")){
            owner = tag.getUUID("owner");
        }
        if(tag.contains("duration")){
            activityEnd = tag.getLong("duration");
        }
        if(tag.contains("state")){
            loadState = LoadState.values()[tag.getInt("state")];
        }
        if(tag.contains("extensions")){
            extensionRange = tag.getInt("extensions");
            extensions = new ExtensionChunkLoaders(level,this);
            extensions.recompute(PlacedExtensionChunkLoader.class,extensionRange,this::createExtension);
        }
        if(tag.contains("pos")) {
            position = BlockPos.of(tag.getLong("pos"));
        }
    }

    @Override
    public void setExtensionRange(int extensionRange) {
        this.extensionRange = extensionRange;
    }

    @Override
    public void setExtensionsMap(ExtensionChunkLoaders extensions) {
        this.extensions=extensions;
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
    public @Nullable ExtensionChunkLoaders getExtensionChunkLoaders() {
        return extensions;
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
        if(hasExceededChunkLimit() || LMCConfig.cost.enabled && activityEnd == -1){
            return LoadState.DISABLED;
        }
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

    @Override
    public void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime) {
        if(!loadState.shouldLoad()){
            return;
        }
        long timeRemaining = activityEnd-gameTime;
        if(LMCConfig.cost.timeSecondsGained/10L >= timeRemaining){
            if(LMCConfig.consumeFuel(level,position.above())){
                activityEnd=gameTime+Math.max(0,timeRemaining)+LMCConfig.cost.timeSecondsGained*20;
                chunkDataModule.updateCheckTime(activityEnd-LMCConfig.cost.timeSecondsGained/10L);
            }
        }
        timeRemaining = activityEnd-gameTime;
        if(timeRemaining <= 0){
            activityEnd = -1;
        }
    }
}
