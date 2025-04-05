//? if cc-tweaked {
/*package com.drathonix.loadmychunks.common.integ.cct.turtle;

import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.integ.cct.peripheral.AbstractChunkLoaderPeripheral;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TurtleChunkLoaderPeripheral extends AbstractChunkLoaderPeripheral {
    private final ITurtleBrainMixin turtle;
    public final TurtleSide side;

    public TurtleChunkLoaderPeripheral(ITurtleBrainMixin turtle, TurtleSide side) {
        //? if >1.16.5 {
        /^if(turtle.getLevel() instanceof ServerLevel) {
        ^///?} else {
        if(turtle.getWorld() instanceof ServerLevel) {
        //?}
            this.turtle = turtle;
            this.side = side;
            turtle.lmc$addToCDM();
        }
        else{
            throw new IllegalStateException("Turtle chunk loader code cannot be accessed on the clientside.");
        }
    }

    @Override
    public @NotNull ChunkDataModule getChunkDataModule() {
        return turtle.lmc$getChunkDataModule();
    }

    @Override
    public @NotNull TurtleChunkLoader getChunkLoader() {
        //     Require that the chunk loader be nonnull (it will be anyways given the peripheral is installed)
        return turtle.lmc$getChunkLoader();
    }

    @Override
    public String getType() {
        return "lmc_chunkloader";
    }

    @Override
    protected @NotNull ServerLevel getLevel() {
        //? if >1.16.5 {
        /^return (ServerLevel) turtle.getLevel();
        ^///?} else {
        return (ServerLevel) turtle.getWorld();
        //?}
    }

    @Override
    protected @NotNull BlockPos getPosition() {
        return getChunkLoader().getPosition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurtleChunkLoaderPeripheral that = (TurtleChunkLoaderPeripheral) o;
        return Objects.equals(turtle, that.turtle) && side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(turtle, side);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return equals((Object)other);
    }
}
*///?}
