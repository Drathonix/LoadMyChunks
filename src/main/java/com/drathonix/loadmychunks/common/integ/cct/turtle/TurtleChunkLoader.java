//? if cc-tweaked {
package com.drathonix.loadmychunks.common.integ.cct.turtle;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.mixin.cct.MixinTurtleBrain;
import com.drathonix.loadmychunks.common.registry.LoaderTypeKeys;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TurtleChunkLoader extends PlacedChunkLoader {
    public TurtleChunkLoader() {}
    public TurtleChunkLoader(BlockPos pos, @Nullable ITurtleBrainMixin turtle, long activityEnd, ILoadState defaultState) {
        super(pos,activityEnd);
        setDefaultState(defaultState);
        if(LMCConfig.cost.enabled && turtle != null) {
            timingsCheck((ServerLevel) turtle.getLevel(), turtle.lmc$getChunkDataModule(), turtle.getLevel().getGameTime());
        }
    }

    public TurtleChunkLoader(BlockPos pos, ITurtleBrainMixin turtle) {
        this(pos,turtle,0, LMCConfig.cct.turtleChunkLoaderDefaultLevel.get());
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypeKeys.CCT_TURTLE_LOADER;
    }

    public TurtleChunkLoader move(ITurtleBrainMixin turtle, BlockPos newPosition) {
        return new TurtleChunkLoader(newPosition, turtle, activityEnd, defaultState);
    }

    @Override
    public boolean supportsExtensions() {
        return false;
    }

    @Override
    public boolean shouldConsumeItems() {
        return super.shouldConsumeItems() && LMCConfig.cct.turtlesConsumeItems;
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

    @Override
    public @NotNull BlockPos getItemSource() {
        return getPosition();
    }
}
//?}
