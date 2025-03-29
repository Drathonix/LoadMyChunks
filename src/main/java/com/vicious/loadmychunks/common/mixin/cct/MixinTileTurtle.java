package com.vicious.loadmychunks.common.mixin.cct;

//? if !cc-tweaked {
/*import com.vicious.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTileTurtle {

}
*///?}
//? if cc-tweaked {

import com.vicious.loadmychunks.common.bridge.IContextDestroyable;
import com.vicious.loadmychunks.common.bridge.IDestroyable;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IHasChunkloader;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
//? if >1.19.2
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//? if <=1.19.2
/*import dan200.computercraft.shared.turtle.blocks.TileTurtle;*/
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
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
            for (TurtleSide value : TurtleSide.values()) {
                IPeripheral p = brain.getPeripheral(value);
                if (p instanceof IContextDestroyable) {
                    ((IContextDestroyable) p).loadMyChunks$destroy(this);
                }
            }
        }
    }

    @Override
    public @Nullable IChunkLoader loadMyChunks$getChunkLoader() {
        if(level instanceof ServerLevel) {
            return ChunkDataManager.getOrCreateChunkData((ServerLevel) level, getBlockPos()).getChunkLoaderAt(getBlockPos());
        }
        return null;
    }

    @Override
    public boolean supportsExtension() {
        return false;
    }
}
//?}
