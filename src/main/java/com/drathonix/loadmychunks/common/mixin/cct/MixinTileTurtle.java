package com.drathonix.loadmychunks.common.mixin.cct;

//? if !computercraft {
import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(LoadMyChunks.class)
public class MixinTileTurtle {

}
//?}
//? if computercraft {

/*import com.drathonix.loadmychunks.common.bridge.IContextDestroyable;
import com.drathonix.loadmychunks.common.bridge.IDestroyable;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITileTurtleMixin;
import com.drathonix.loadmychunks.common.integ.cct.bridge.ITurtleBrainMixin;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.TurtleSide;
//? if >1.19.2
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
//? if <=1.19.2
/^import dan200.computercraft.shared.turtle.blocks.TileTurtle;^/
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

//? if >1.19.2
@Mixin(value = TurtleBlockEntity.class,remap = false)
//? if <=1.19.2
/^@Mixin(value = TileTurtle.class,remap = false)^/
public abstract class MixinTileTurtle implements IDestroyable,IHasChunkloader, ITileTurtleMixin {
    @Shadow
    private TurtleBrain brain;

    @Override
    public ITurtleBrainMixin loadMyChunks$getBrain() {
        return (ITurtleBrainMixin)brain;
    }

    @Override
    public void loadMyChunks$destroy() {
        MultiversioningHelper.serverLevel((BlockEntity)(Object)this, sl-> {
            BlockPos pos = ((BlockEntity)(Object)this).getBlockPos();
            TurtleChunkLoader query = ((ITurtleBrainMixin)brain).lmc$getChunkLoader().move(pos);
            ChunkDataManager.removeChunkLoader(sl,pos,query);
        });
    }

    @Override
    public @Nullable IChunkLoader loadMyChunks$getChunkLoader() {
        MultiversioningHelper.serverLevel(((BlockEntity)(Object)this),sl->{
            BlockPos pos = ((BlockEntity)(Object)this).getBlockPos();
            return ChunkDataManager.getOrCreateChunkData(sl, pos).getChunkLoaderAt(pos);
        });
        return null;
    }
}
*///?}
