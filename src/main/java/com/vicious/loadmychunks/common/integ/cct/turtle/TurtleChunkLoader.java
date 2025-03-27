//? if cct {
package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.registry.LoaderTypeKeys;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.control.ILoadState;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.vicious.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

public class TurtleChunkLoader extends PlacedChunkLoader {
    @Nullable protected TurtleChunkLoaderPeripheral peripheral;
    protected boolean loadExtensions = false;
    public TurtleChunkLoader() {}
    public TurtleChunkLoader(BlockPos pos, @Nullable TurtleChunkLoaderPeripheral peripheral, int r, ExtensionChunkLoaders extensions, long activityEnd) {
        super(pos,activityEnd);
        this.extensionRange=r;
        this.extensions = extensions;
        this.peripheral=peripheral;
        if(LMCConfig.cost.enabled && peripheral != null) {
            timingsCheck(peripheral.getLevel(), peripheral.getChunkDataModule(), peripheral.getLevel().getGameTime());
        }
    }

    public TurtleChunkLoader(BlockPos pos) {
        super(pos);
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypeKeys.CCT_TURTLE_LOADER;
    }

    public TurtleChunkLoader move(TurtleChunkLoaderPeripheral peripheral, BlockPos newPosition) {
        TurtleChunkLoader moved = new TurtleChunkLoader(newPosition, peripheral, extensionRange, extensions,activityEnd);
        if(moved.extensions != null) {
            moved.extensions.recompute(TurtleExtensionChunkLoader.class, extensionRange, this::createTurtleExtension);
        }
        return moved;
    }

    @Override
    public boolean supportsExtensions() {
        return false;
    }

    public TurtleExtensionChunkLoader createTurtleExtension(ChunkPos position) {
        if(peripheral == null) {
            throw new IllegalStateException("No peripheral.");
        }
        return new TurtleExtensionChunkLoader(position, peripheral.getMutableChunkLoader());
    }

    @Override
    protected boolean outOfTime() {
        if(!LMCConfig.cct.turtlesConsumeItems){
            return false;
        }
        return super.outOfTime();
    }

    @Override
    public ExtensionChunkLoaders.Factory<?> getExtensionFactory() {
        return this::createTurtleExtension;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IExtensionChunkLoader<?>> Class<T> getExtensionClass() {
        return (Class<T>) TurtleExtensionChunkLoader.class;
    }

    public void setPeripheral(@Nullable TurtleChunkLoaderPeripheral peripheral) {
        this.peripheral = peripheral;
    }

    @Override
    public ILoadState getActiveState() {
        if(!LMCConfig.cct.enableTurtleChunkLoading){
            return LoadStateEnum.DISABLED;
        }
        ILoadState loadState = super.getActiveState();
        if(LMCConfig.cct.ignoreTickChecks && loadState.shouldLoad()){
            return LoadStateEnum.PERMANENT;
        }
        return loadState;
    }

    public ILoadState getExtensionLoadState() {
        return loadExtensions ? loadState : LoadStateEnum.DISABLED;
    }

    @Override
    public void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime) {
        if(!loadState.shouldLoad()){
            return;
        }
        long timeRemaining = activityEnd-gameTime;
        if(LMCConfig.cost.timeSecondsGained/10L >= timeRemaining){
            if(LMCConfig.consumeFuel(level, getPosition(),1+getExtensionCount())){
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
//?}
