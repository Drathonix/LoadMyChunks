package com.drathonix.loadmychunks.common.mixin.cct;

//? if !cc-tweaked {
/*import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTurtleBrain {

}
*///?}
//? if cc-tweaked {

import com.drathonix.loadmychunks.common.bridge.IContextDestroyable;
import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.util.Other;
import dan200.computercraft.api.peripheral.IPeripheral;
//? if >=1.20.6
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.core.util.PeripheralHelpers;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = TurtleBrain.class,remap = false)
public abstract class MixinTurtleBrain implements ITurtleBrainMixin {
    @Shadow public abstract IPeripheral getPeripheral(TurtleSide side);

    @Shadow public abstract Level getLevel();

    @Shadow @NotNull
    public abstract BlockPos getPosition();

    @Unique
    public AtomicReference<TurtleChunkLoader> lmc$chunkLoader = new AtomicReference<>();
    @Unique
    public ChunkDataModule cdm;

    @Inject(method = "<init>",at= @At(value = "RETURN"))
    private void lmc$postInit(TurtleBlockEntity turtle, CallbackInfo ci){
        Other.serverLevel(turtle,sl->{
            BlockPos newPosition = turtle.getBlockPos();
            this.cdm = ChunkDataManager.getOrCreateChunkData(sl,newPosition);
            if(LMCConfig.cct.turtlesChunkLoadWithoutPeripheral){
                this.lmc$chunkLoader.set(ChunkDataManager.computeChunkLoaderIfAbsent(sl,newPosition,TurtleChunkLoader.class,loader->loader.getPosition().equals(newPosition),()->new TurtleChunkLoader(newPosition, this)));
            }
        });
    }

    @Inject(method = "updatePeripherals", at = @At("TAIL"))
    public void lmc$checkShouldUnload(ServerComputer serverComputer, CallbackInfo ci){
        if(lmc$shouldChunkLoad()){
            return;
        }
        lmc$removeChunkLoader();
    }

    @Override
    public void lmc$removeChunkLoader() {
        Other.serverLevel(getLevel(), sl-> Optional.ofNullable(lmc$chunkLoader.get()).ifPresent(loader->{
            cdm.consumeLoadState(previous -> {
                cdm.removeLoader(sl, loader);
                cdm.updateChunkLoadState(sl, previous);
            });
            ChunkDataManager.setDirty(sl);
        }));
    }

    @Unique
    public boolean lmc$shouldChunkLoad(){
        boolean stable = LMCConfig.cct.turtlesChunkLoadWithoutPeripheral;
        if(stable) {
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
    
    @Override
    public boolean lmc$preMove(ServerLevel oldWorld, BlockPos newPosition) {
        boolean stable = lmc$shouldChunkLoad();
        if(stable) {
            Optional.ofNullable(lmc$chunkLoader.get()).ifPresent(loader -> ChunkDataManager.addChunkLoader(oldWorld, newPosition, lmc$chunkLoader.get().move(this, newPosition)));
        }
        return stable;
    }

    @Inject(method="setOwner",at = @At("RETURN"))
    public void lmc$postMove(TurtleBlockEntity owner, CallbackInfo ci) {
        BlockPos newPosition = getPosition();
        ServerLevel sl = (ServerLevel)getLevel();
        this.lmc$chunkLoader.set(ChunkDataManager.computeChunkLoaderIfAbsent(sl,newPosition,TurtleChunkLoader.class,loader->loader.getPosition().equals(newPosition),()->new TurtleChunkLoader(newPosition,this)));
        cdm = ChunkDataManager.getOrCreateChunkData(sl, newPosition);
        ChunkDataManager.setDirty(sl);
    }

    @Override
    public TurtleChunkLoader lmc$getOrCreateChunkLoader() {
        return Optional.ofNullable(this.lmc$chunkLoader.get()).orElseGet(()->{
            BlockPos newPosition = getPosition();
            ServerLevel sl = (ServerLevel)getLevel();
            TurtleChunkLoader out =ChunkDataManager.computeChunkLoaderIfAbsent(sl,newPosition,TurtleChunkLoader.class,loader->loader.getPosition().equals(newPosition),()->new TurtleChunkLoader(newPosition,this));
            this.lmc$chunkLoader.set(out);
            return out;
        });
    }
}
//?}
