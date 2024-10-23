package com.vicious.loadmychunks.common.mixin.cct;

//? if !cct {
import com.vicious.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTurtleMoveCommand {

}
//?}
//? if cct {

/*import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
//? if <=1.19.2
/^import dan200.computercraft.ComputerCraft;^/
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
//? if <=1.19.2
/^import dan200.computercraft.shared.TurtlePermissions;^/
import dan200.computercraft.shared.turtle.core.TurtleMoveCommand;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("all")
@Mixin(value = TurtleMoveCommand.class
//? if forge && <=1.20.1
, remap=false
)
public class MixinTurtleMoveCommand {

    //? if <=1.19.2 {
    /^@Redirect(method = "execute",at = @At(value = "INVOKE", target = "Ldan200/computercraft/api/turtle/ITurtleAccess;teleportTo(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")
            //? if forge && <=1.16.5
            ,remap = true
    )
    public boolean checkCanTP(ITurtleAccess turtle, Level oldWorld, BlockPos newPosition){
        BlockPos oldPosition = turtle.getPosition();
        boolean stable = oldWorld.isLoaded(newPosition);
        if(oldWorld instanceof ServerLevel) {
            for (TurtleSide side : TurtleSide.values()) {
                IPeripheral peripheral = turtle.getPeripheral(side);
                if (peripheral instanceof TurtleChunkLoaderPeripheral) {
                    stable = true;
                    ChunkDataManager.addChunkLoader((ServerLevel) oldWorld, newPosition, ((TurtleChunkLoaderPeripheral) peripheral).getChunkLoader().move(newPosition));
                }
            }
        }
        if(stable){
            return turtle.teleportTo(oldWorld,newPosition);
        }
        return stable;
    }
    ^///?}

    //? >1.19.2 {
    @Inject(method = "execute",at = @At(value = "INVOKE",target = "Ldan200/computercraft/api/turtle/ITurtleAccess;teleportTo(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void changeLogic(ITurtleAccess turtle, CallbackInfoReturnable<TurtleCommandResult> cir, Direction direction, ServerLevel oldWorld, BlockPos oldPosition, BlockPos newPosition) {
        boolean stable = oldWorld.isLoaded(newPosition);
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
    //?}

    @Inject(method = "execute",at = @At(value = "RETURN"),remap = false)
    public void postMove(ITurtleAccess turtle, CallbackInfoReturnable<TurtleCommandResult> cir){
        if(cir.getReturnValue().isSuccess()) {
            for (TurtleSide side : TurtleSide.values()) {
                IPeripheral peripheral = turtle.getPeripheral(side);
                if (peripheral instanceof TurtleChunkLoaderPeripheral) {
                    ((TurtleChunkLoaderPeripheral) peripheral).setPosition(turtle.getPosition());
                }
            }
        }
    }

    /^*
     * @author Drathonix
     * @reason If anyone else touches the original method, I will be very unhappy.
     ^/
    @Overwrite
    private static TurtleCommandResult canEnter(TurtlePlayer turtlePlayer,
                                                //? if >1.19.2
                                                ServerLevel world,
                                                //? if <=1.19.2
                                                /^Level world,^/
                                                BlockPos position) {
        if (world.isOutsideBuildHeight(position)) {
            return TurtleCommandResult.failure(position.getY() < 0 ? "Too low to move" : "Too high to move");
        }
        if (!world.isInWorldBounds(position)) return TurtleCommandResult.failure("Cannot leave the world");

        // Check spawn protection
        //? if <=1.19.2
        /^if( ComputerCraft.turtlesObeyBlockProtection && !TurtlePermissions.isBlockEnterable( world, position, turtlePlayer )) {^/
        //? if >1.19.2
        if (turtlePlayer.isBlockProtected(world, position)) {
            return TurtleCommandResult.failure("Cannot enter protected area");
        }

        if (!world.getWorldBorder().isWithinBounds(position)) {
            return TurtleCommandResult.failure("Cannot pass the world border");
        }

        return TurtleCommandResult.success();
    }
}
*///?}
