//? if cct {
package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.bridge.IContextDestroyable;
import com.vicious.loadmychunks.common.integ.cct.peripheral.AbstractChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TurtleChunkLoaderPeripheral extends AbstractChunkLoaderPeripheral implements IContextDestroyable {
    private final ITurtleAccess turtle;
    private final TurtleSide side;
    private TurtleChunkLoader chunkLoader;
    private ChunkDataModule cdm;

    public TurtleChunkLoaderPeripheral(ITurtleAccess turtle, TurtleSide side) {
        //? if >1.16.5
        /*if(turtle.getLevel() instanceof ServerLevel) {*/
        //? if <=1.16.5
        if(turtle.getWorld() instanceof ServerLevel) {
            this.turtle = turtle;
            this.side = side;
            //? if >1.16.5 {
            /*this.cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) turtle.getLevel(),turtle.getPosition());
            this.chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent((ServerLevel) turtle.getLevel(),turtle.getPosition(),TurtleChunkLoader.class,loader-> loader.getPosition().equals(turtle.getPosition()),()->new TurtleChunkLoader(turtle.getPosition()));
            *///?}
            //? if <=1.16.5 {
            this.cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) turtle.getWorld(),turtle.getPosition());
            this.chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent((ServerLevel) turtle.getWorld(),turtle.getPosition(),TurtleChunkLoader.class,loader-> loader.getPosition().equals(turtle.getPosition()),()->new TurtleChunkLoader(turtle.getPosition()));
            //?}
        }
        else{
            throw new IllegalStateException("Turtle chunk loader code cannot be accessed on the clientside.");
        }
    }

    @Override
    public ChunkDataModule getChunkDataModule() {
        return cdm;
    }

    @Override
    public String getType() {
        return "lmc_chunkloader";
    }

    @Override
    protected ServerLevel getLevel() {
        //? if >1.16.5
        /*return (ServerLevel) turtle.getLevel();*/
        //? if <=1.16.5
        return (ServerLevel) turtle.getWorld();
    }

    @Override
    protected BlockPos getPosition() {
        return chunkLoader.getPosition();
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

    public TurtleChunkLoader getChunkLoader() {
        return chunkLoader;
    }

    @Override
    public void loadMyChunks$destroy(Object context) {
        IPeripheral opposite = turtle.getPeripheral(side == TurtleSide.LEFT ? TurtleSide.RIGHT : TurtleSide.LEFT);
        if (context instanceof BlockEntity || !(opposite instanceof AbstractChunkLoaderPeripheral)) {
            cdm.removeLoader(chunkLoader);
            cdm.updateChunkLoadState(getLevel());
            ChunkDataManager.setDirty(getLevel());
        }
    }

    public void setPosition(BlockPos newPosition) {
        chunkLoader = ChunkDataManager.computeChunkLoaderIfAbsent(getLevel(),newPosition, TurtleChunkLoader.class,loader->loader.getPosition().equals(newPosition),()->chunkLoader);
        cdm = ChunkDataManager.getOrCreateChunkData(getLevel(), newPosition);
        ChunkDataManager.setDirty(getLevel());
    }
}
//?}
