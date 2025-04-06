package com.drathonix.loadmychunks.common.integ.cct.bridge;

//? if cc-tweaked {
import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

// Everything of ITurtleBrainMixin will already be an ITurtleAccess
@SuppressWarnings("NonExtendableApiUsage")
@ApiStatus.NonExtendable
public interface ITurtleBrainMixin extends ITurtleAccess {
    /**
     * Removes the chunk loader from the {@link com.drathonix.loadmychunks.common.system.ChunkDataManager}
     */
    default void lmc$removeChunkLoader() {
        MultiversioningHelper.serverLevel(getLevel(), sl-> {
            ChunkDataModule lmc$cdm = lmc$getChunkDataModule();
            lmc$cdm.consumeLoadState(previous -> {
                lmc$cdm.removeLoader(sl, lmc$getChunkLoader());
                lmc$cdm.updateChunkLoadState(sl, previous);
            });
            ChunkDataManager.setDirty(sl);
        });
    }
    /**
     * Called by {@link com.drathonix.loadmychunks.common.mixin.cct.MixinTurtleMoveCommand} before moving.
     * @param oldWorld the level.
     * @param newPosition the destination block.
     * @return whether the destination is chunk loaded.
     */
    boolean lmc$preMove(ServerLevel oldWorld, BlockPos newPosition);

    /**
     * Gets the turtle's current {@link com.drathonix.loadmychunks.common.system.ChunkDataManager}
     * @return the chunk data module for the current chunk.
     */
    @NotNull
    ChunkDataModule lmc$getChunkDataModule();

    /**
     * Gets the turtle's current {@link TurtleChunkLoader}
     * @return the current turtle chunk loader.
     */
    @NotNull
    TurtleChunkLoader lmc$getChunkLoader();

    /**
     * Whether the turtle can load chunks.
     * @return whether the turtle cn load chunks.
     */
    default boolean lmc$shouldChunkLoad(){
        if(LMCConfig.cct.turtlesChunkLoadWithoutPeripheral) {
            return true;
        }
        for (TurtleSide side : TurtleSide.values()) {
            IPeripheral peripheral = getPeripheral(side);
            if (peripheral instanceof TurtleChunkLoaderPeripheral) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the turtle chunk loader to its CDM.
     */
    default void lmc$addToCDM() {
        MultiversioningHelper.serverLevel(getLevel(), sl->{
            lmc$getChunkDataModule().addLoader(sl,lmc$getChunkLoader());
            ChunkDataManager.setDirty(sl);
        });
    }

    //? if <=1.16.5 {
    /*@NotNull
    default Level getLevel() {
        return getWorld();
    }
    *///?}
}
//?}
