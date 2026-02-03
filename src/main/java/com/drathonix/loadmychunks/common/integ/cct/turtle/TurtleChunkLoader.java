//? if computercraft {
package com.drathonix.loadmychunks.common.integ.cct.turtle;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITileTurtleMixin;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.registry.LoaderTypeKeys;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.loaders.DoNotAddException;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
//? if >1.19.2 {
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//?} else {
/*import dan200.computercraft.shared.turtle.blocks.TileTurtle;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
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
    }

    @Override
    public boolean postLoad(ChunkAccess chunk) throws DoNotAddException {
        super.postLoad(chunk);
        //TODO: fix an infinite loop caused by this.
        //BlockEntity target = chunk.getBlockEntity(position);
        // if >1.19.2 {
        //if(target instanceof ITileTurtleMixin){
        //    ITurtleBrainMixin mixin = ((ITileTurtleMixin) target).loadMyChunks$getBrain();
        //    setTurtle(mixin);
        //}
        //if(target instanceof TurtleBlockEntity){
        //    ((TurtleBlockEntity) target).createServerComputer().turnOn();
        //}
        //} else {
        /*if(target instanceof TileTurtle){
            ((TileTurtle) target).createServerComputer().turnOn();
        }
        *///}
        //return true;
        return false;
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
        if(!LMCConfig.cct.enableTurtleChunkLoading || !Optional.ofNullable(turtle).map(ITurtleBrainMixin::lmc$shouldChunkLoad).orElse(true)){
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
//?}
