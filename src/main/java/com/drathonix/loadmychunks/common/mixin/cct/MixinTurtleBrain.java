package com.drathonix.loadmychunks.common.mixin.cct;

//? if !cc-tweaked {
/*import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTurtleBrain {

}
*///?}
//? if cc-tweaked {

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.computer.core.ServerComputer;
//? if >=1.19.4 {
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//?} else {
/*import dan200.computercraft.shared.turtle.blocks.TileTurtle;
*///?}
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = TurtleBrain.class,remap = false)
public abstract class MixinTurtleBrain implements ITurtleBrainMixin {
    @Shadow public abstract IPeripheral getPeripheral(TurtleSide side);

    //? if >1.16.5 {
    @Shadow public abstract Level getLevel();
    //?} else {
    /*@Shadow public abstract Level getWorld();
    *///?}

    @Shadow @NotNull
    public abstract BlockPos getPosition();

    @Unique
    public AtomicReference<TurtleChunkLoader> lmc$chunkLoader = new AtomicReference<>();
    @Unique
    @Nullable
    public ChunkDataModule lmc$cdm;

    @Inject(method = "updatePeripherals", at = @At("TAIL"))
    public void lmc$checkShouldUnload(ServerComputer serverComputer, CallbackInfo ci){
        if(lmc$shouldChunkLoad()){
            lmc$addToCDM();
        } else {
            lmc$removeChunkLoader();
        }
    }

    @Inject(method="setupComputer",at = @At("HEAD"))
    public void setup(ServerComputer computer, CallbackInfo ci){
        if(lmc$shouldChunkLoad()) {
            lmc$addToCDM();
        }
    }

    @Override
    public boolean lmc$preMove(ServerLevel sl, BlockPos newPosition) {
        boolean stable = lmc$shouldChunkLoad();
        if(stable) {
            ChunkDataManager.computeChunkLoaderIfAbsent(sl,newPosition,TurtleChunkLoader.class,loader->loader.getPosition().equals(newPosition),()->lmc$chunkLoader.get().move(newPosition));
            return ChunkDataManager.getOrCreateChunkData(sl,newPosition).getLoadState().shouldLoad();
        }
        return false;
    }

    @Inject(method="setOwner",at = @At("RETURN"))
    //? if >1.19.2 {
    public void lmc$postMove(TurtleBlockEntity owner, CallbackInfo ci) {
    //?} else {
    /*public void lmc$postMove(TileTurtle owner, CallbackInfo ci) {
    *///?}
        MultiversioningHelper.serverLevel(owner,sl->{
            BlockPos newPosition = getPosition();
            this.lmc$chunkLoader.set(ChunkDataManager.computeChunkLoaderIfAbsent(sl,newPosition,TurtleChunkLoader.class,lmc$shouldChunkLoad(),loader->loader.getPosition().equals(newPosition),()-> new TurtleChunkLoader(newPosition,this)));
            this.lmc$chunkLoader.get().setTurtle(this);
            lmc$cdm = ChunkDataManager.getOrCreateChunkData(sl, newPosition);
            ChunkDataManager.setDirty(sl);
        });
    }

    @Override
    public @NotNull TurtleChunkLoader lmc$getChunkLoader() {
        if (lmc$chunkLoader.get() == null) {
            //? if >1.16.5 {
            MultiversioningHelper.serverLevel(getLevel(), sl-> {
            //?} else {
            /*MultiversioningHelper.serverLevel(getWorld(), sl-> {
            *///?}
                BlockPos pos = getPosition();
                lmc$chunkLoader.set(ChunkDataManager.computeChunkLoaderIfAbsent(
                        sl,
                        pos,
                        TurtleChunkLoader.class,
                        lmc$shouldChunkLoad(),
                        cl -> cl.getPosition().equals(pos),
                        () -> new TurtleChunkLoader(pos, this)
                ));
                lmc$chunkLoader.get().setTurtle(this);
            });
        }
        return lmc$chunkLoader.get();
    }

    @Override
    public @NotNull ChunkDataModule lmc$getChunkDataModule() {
        if(lmc$cdm == null) {
            //? if >1.16.5 {
            MultiversioningHelper.serverLevel(getLevel(), sl-> {
            //?} else {
            /*MultiversioningHelper.serverLevel(getWorld(), sl-> {
            *///?}
                this.lmc$cdm = ChunkDataManager.getOrCreateChunkData(sl, getPosition());
            });
        }
        return lmc$cdm;
    }
}
//?}
