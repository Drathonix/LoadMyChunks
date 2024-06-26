package com.vicious.loadmychunks.common.mixin.cct;

import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleMoveCommand;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("all")
@Mixin(value = TurtleMoveCommand.class
//? if forge && <=1.20.1
, remap=false
)
public class MixinTurtleMoveCommand {

    @Inject(method = "execute",at = @At(value = "INVOKE",target = "Ldan200/computercraft/api/turtle/ITurtleAccess;teleportTo(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void changeLogic(ITurtleAccess turtle, CallbackInfoReturnable<TurtleCommandResult> cir, Direction direction, ServerLevel oldWorld, BlockPos oldPosition, BlockPos newPosition) {
        boolean stable = oldWorld.getWorldBorder().isWithinBounds(oldPosition);
        for (TurtleSide side : TurtleSide.values()) {
            IPeripheral peripheral = turtle.getPeripheral(side);
            if (peripheral instanceof TurtleChunkLoaderPeripheral) {
                stable = true;
                ChunkDataManager.addChunkLoader(oldWorld, newPosition, ((TurtleChunkLoaderPeripheral) peripheral).getChunkLoader().move(newPosition));
            }
        }
        if(!stable){
            cir.setReturnValue(TurtleCommandResult.failure("Cannot enter unloaded area"));
        }
    }

    @Inject(method = "execute",at = @At(value = "INVOKE", target = "Ldan200/computercraft/api/turtle/ITurtleAccess;consumeFuel(I)Z",shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT,remap = false)
    public void postMove(ITurtleAccess turtle, CallbackInfoReturnable<TurtleCommandResult> cir, Direction direction, ServerLevel oldWorld, BlockPos oldPosition, BlockPos newPosition){
        for (TurtleSide side : TurtleSide.values()) {
            IPeripheral peripheral = turtle.getPeripheral(side);
            if (peripheral instanceof TurtleChunkLoaderPeripheral) {
                ((TurtleChunkLoaderPeripheral)peripheral).setPosition(newPosition);
            }
        }
    }
    /**
     * @author Drathonix
     * @reason If anyone else touches the original method, I will be very unhappy.
     */
    @Overwrite
    private static TurtleCommandResult canEnter(TurtlePlayer turtlePlayer, ServerLevel world, BlockPos position) {
        if (world.isOutsideBuildHeight(position)) {
            return TurtleCommandResult.failure(position.getY() < 0 ? "Too low to move" : "Too high to move");
        }
        if (!world.isInWorldBounds(position)) return TurtleCommandResult.failure("Cannot leave the world");

        // Check spawn protection
        if (turtlePlayer.isBlockProtected(world, position)) {
            return TurtleCommandResult.failure("Cannot enter protected area");
        }

        if (!world.getWorldBorder().isWithinBounds(position)) {
            return TurtleCommandResult.failure("Cannot pass the world border");
        }

        return TurtleCommandResult.success();
    }
}
