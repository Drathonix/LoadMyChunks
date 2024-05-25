package com.vicious.loadmychunks.mixins;

import com.vicious.loadmychunks.bridge.IChunkMapMixin;
import com.vicious.loadmychunks.bridge.ILevelChunkMixin;
import com.vicious.loadmychunks.bridge.ILevelMixin;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Level.class)
public abstract class MixinLevel implements ILevelMixin {
    @Shadow private boolean tickingBlockEntities;

    @Shadow @Final protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow public abstract LevelChunk getChunk(int i, int j);


    @Shadow public abstract boolean isClientSide();

    /**
     * Overrides the default block ticking logic by ticking each chunk's tile entities in groups rather than all TEs individually.
     */
    //TODO: investigate if this has significant mod conflicts.
    @Inject(method = "tickBlockEntities",at = @At(value = "INVOKE",target = "Ljava/util/List;iterator()Ljava/util/Iterator;",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void tickChunkWise(CallbackInfo ci, ProfilerFiller profilerFiller){
        if(!this.isClientSide()) {
            //noinspection resource
            if (loadMyChunks$cast().getChunkSource() instanceof ServerChunkCache scc) {
                Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = ((IChunkMapMixin) scc.chunkMap).loadMyChunks$getUpdatingChunkMap();
                for (ChunkHolder value : updatingChunkMap.values()) {
                    if (value.getTickingChunk() instanceof ILevelChunkMixin chunk) {
                        if (scc.chunkMap.getDistanceManager().inBlockTickingRange(chunk.loadMyChunks$posAsLong())) {
                            chunk.loadMyChunks$tick();
                        }
                    }
                }
            }
            this.tickingBlockEntities = false;
            profilerFiller.pop();
            ci.cancel();
        }
    }

    @Override
    public void loadMyChunks$removeTicker(TickingBlockEntity tickingBlockEntity) {
        blockEntityTickers.remove(tickingBlockEntity);
    }

    @Unique
    public Level loadMyChunks$cast(){
        return Level.class.cast(this);
    }
}
