package com.drathonix.loadmychunks.common.system.loaders;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.registry.LoaderTypeKeys;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.drathonix.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.extension.PlacedExtensionChunkLoader;
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
    protected ILoadState defaultState = LoadStateRegistry.TICKING;
    protected ILoadState loadState = defaultState;
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
        loadState.putCompound("state",tag);
        defaultState.putCompound("default",tag);
        tag.putLong("pos",position.asLong());
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException {
        if(tag.contains("pos")) {
            position = BlockPos.of(tag.getLong("pos"));
            if(!(level.getBlockEntity(position) instanceof IHasChunkloader)){
                throw new DoNotAddException();
            }
        }
        if(tag.contains("owner")){
            owner = tag.getUUID("owner");
        }
        if(tag.contains("duration")){
            activityEnd = tag.getLong("duration");
        }
        defaultState = LoadStateRegistry.fromCompound("default",tag,LoadStateRegistry.TICKING);
        loadState = LoadStateRegistry.fromCompound("state",tag,defaultState);
        if(tag.contains("extensions")){
            extensionRange = tag.getInt("extensions");
            extensions = new ExtensionChunkLoaders(level,this);
            extensions.recompute(PlacedExtensionChunkLoader.class,extensionRange,this::createExtension);
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
    public boolean supportsEntityTicking() {
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

    protected boolean outOfTime(){
        return LMCConfig.cost.enabled && activityEnd == -1;
    }

    @Override
    public ILoadState getActiveState() {
        if(hasExceededChunkLimit() || outOfTime()){
            return LoadStateEnum.DISABLED;
        }
        return loadState;
    }

    @Override
    public ILoadState getDefaultState() {
        return defaultState;
    }

    @Override
    public void setActiveState(ILoadState state) {
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
        return LoaderTypeKeys.PLACED_LOADER;
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
    public @NotNull ChunkPos getChunkPos() {
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

    @Override
    public ILoadState setDefaultState(ILoadState defaultState) {
        ILoadState prev = this.defaultState;
        this.defaultState = defaultState;
        // Will be this unless disabled by CCT
        if(this.loadState == LoadStateRegistry.TICKING){
            this.loadState=defaultState;
        }
        return prev;
    }
}
