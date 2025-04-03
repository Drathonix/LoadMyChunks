package com.drathonix.loadmychunks.common.mixin.cct;

//? if !cc-tweaked {
/*import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTileTurtle {

}
*///?}
//? if cc-tweaked {

import com.drathonix.loadmychunks.common.bridge.IContextDestroyable;
import com.drathonix.loadmychunks.common.bridge.IDestroyable;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
//? if >1.19.2
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//? if <=1.19.2
/*import dan200.computercraft.shared.turtle.blocks.TileTurtle;*/
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if >1.19.2
@Mixin(value = TurtleBlockEntity.class,remap = false)
//? if <=1.19.2
/*@Mixin(value = TileTurtle.class,remap = false)*/
public abstract class MixinTileTurtle extends MixinAbstractComputerBlockEntity implements IDestroyable,IHasChunkloader {
    @Shadow
    private TurtleBrain brain;

    @Override
    public void loadMyChunks$destroy() {
        if(getLevel() instanceof ServerLevel) {
            ((ITurtleBrainMixin)brain).lmc$removeChunkLoader();
        }
    }

    @Override
    public @Nullable IChunkLoader loadMyChunks$getChunkLoader() {
        if(level instanceof ServerLevel) {
            return ChunkDataManager.getOrCreateChunkData((ServerLevel) level, getBlockPos()).getChunkLoaderAt(getBlockPos());
        }
        return null;
    }
}
//?}
