//? if computercraft {
/*package com.drathonix.loadmychunks.common.integ.cct.turtle;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.mixin.cct.MixinTurtleBrain;
import com.drathonix.loadmychunks.common.registry.LoaderTypeKeys;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.loaders.DoNotAddException;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
//? if >1.19.2 {
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
//?} else {
/^import dan200.computercraft.shared.computer.blocks.TileComputerBase;
^///?}
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TurtleChunkLoader extends PlacedChunkLoader {
    @Nullable
    private ITurtleBrainMixin turtle;

    public TurtleChunkLoader() {}
    public TurtleChunkLoader(BlockPos pos, @NotNull ITurtleBrainMixin turtle, long activityEnd, ILoadState defaultState) {
        super(pos,activityEnd);
        setDefaultState(defaultState);
        if(LMCConfig.cost.enabled) {
            timingsCheck((ServerLevel) turtle.getLevel(), turtle.lmc$getChunkDataModule(), turtle.getLevel().getGameTime());
        }
        this.turtle=turtle;
    }

    public TurtleChunkLoader(BlockPos pos, ITurtleBrainMixin turtle) {
        this(pos,turtle,0, LMCConfig.cct.turtleChunkLoaderDefaultLevel.get());
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypeKeys.CCT_TURTLE_LOADER;
    }

    public TurtleChunkLoader move(BlockPos newPosition) {
        return new TurtleChunkLoader(newPosition, turtle, activityEnd, defaultState);
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException {
        super.load(tag, level);
        // Force the computer online.
        BlockEntity target = level.getBlockEntity(position);
        //? if >1.19.2 {
        if(target instanceof AbstractComputerBlockEntity){
            ((AbstractComputerBlockEntity) target).createServerComputer().turnOn();
        }
        //?} else {
        /^if(target instanceof TileComputerBase){
            ((TileComputerBase) target).createServerComputer().turnOn();
        }
        ^///?}
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
        if(!LMCConfig.cct.enableTurtleChunkLoading || !Optional.ofNullable(turtle).map(ITurtleBrainMixin::lmc$shouldChunkLoad).orElse(false)){
            return LoadStateEnum.DISABLED;
        }
        ILoadState loadState = super.getActiveState();
        if(LMCConfig.cct.ignoreTickChecks && loadState.shouldLoad()){
            return LoadStateEnum.PERMANENT;
        }
        return loadState;
    }

    public void setTurtle(@Nullable ITurtleBrainMixin turtle) {
        this.turtle = turtle;
    }

    @Override
    public @NotNull BlockPos getItemSource() {
        return getPosition();
    }
}
*///?}
