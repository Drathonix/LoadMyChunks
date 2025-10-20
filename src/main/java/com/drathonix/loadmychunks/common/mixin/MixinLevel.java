package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.bridge.ILevelMixin;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
//? if >1.21.1 {
/*import net.minecraft.util.profiling.Profiler;
*///?}
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
//? if >1.16.5 {
import net.minecraft.world.level.block.entity.TickingBlockEntity;
//?}
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Mixin(value = Level.class,priority = 0)
public abstract class MixinLevel implements ILevelMixin {
    //? if >1.16.5 {
    @Shadow @Final protected List<TickingBlockEntity> blockEntityTickers;


    @Shadow public abstract boolean isClientSide();
    @Unique private static final Iterator<TickingBlockEntity> lmc$emptyIter = Collections.emptyIterator();

    //? if <1.21.2 {
    @Shadow public abstract ProfilerFiller getProfiler();
    //?} else {
    /*public ProfilerFiller getProfiler(){
        return Profiler.get();
    }
    *///?}
    /**
     * Overrides the default block ticking logic by ticking each chunk's tile entities in groups rather than all TEs individually.
     */
    @Redirect(method = "tickBlockEntities",at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    public Iterator<TickingBlockEntity> tickChunkWise(List<TickingBlockEntity> instance){
        if(!this.isClientSide()){
            //noinspection resource
            if (loadMyChunks$cast().getChunkSource() instanceof ServerChunkCache scc) {
                Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = ((IChunkMapMixin) scc.chunkMap).lmc$getUpdatingChunkMap();
                synchronized (updatingChunkMap) {
                    for (ChunkHolder value : updatingChunkMap.values()) {
                        if (value != null && value.getTickingChunk() instanceof ILevelChunkMixin chunk) {
                            if (scc.chunkMap.getDistanceManager().inBlockTickingRange(chunk.loadMyChunks$posAsLong())) {
                                chunk.loadMyChunks$tick();
                            }
                        }
                    }
                }
            }
            return lmc$emptyIter;
        } else {
            return instance.iterator();
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
    //?}

    //TODO: Remove redundancies
    //? if <=1.16.5 {

    /*@Unique private static final Iterator<BlockEntity> lmc$emptyIter = Collections.emptyIterator();

    @Shadow public abstract boolean isClientSide();

    @Shadow @Final public List<BlockEntity> tickableBlockEntities;

    @Shadow @Final public List<BlockEntity> blockEntityList;

    @Shadow public abstract ProfilerFiller getProfiler();

    /^*
     * Overrides the default block ticking logic by ticking each chunk's tile entities in groups rather than all TEs individually.
     *
     * @return An empty list to spoof the original method
     ^/
    //TODO: investigate if this has significant mod conflicts.
    @Redirect(method = "tickBlockEntities",at = @At(value = "INVOKE",target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    public Iterator<BlockEntity> tickChunkWise(List<BlockEntity> instance){
        if(!this.isClientSide()) {
            //noinspection resource
            if (loadMyChunks$cast().getChunkSource() instanceof ServerChunkCache) {
                ServerChunkCache scc = (ServerChunkCache) loadMyChunks$cast().getChunkSource();
                Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = ((IChunkMapMixin) scc.chunkMap).lmc$getUpdatingChunkMap();
                for (ChunkHolder value : updatingChunkMap.values()) {
                    if(value != null) {
                        LevelChunk tickingChunk = value.getTickingChunk();
                        if (tickingChunk instanceof ILevelChunkMixin) {
                            if (scc.isTickingChunk(tickingChunk.getPos().getWorldPosition())) {
                                ((ILevelChunkMixin)tickingChunk).loadMyChunks$tick(getProfiler());
                            }
                        }
                    }
                }
            }
            return lmc$emptyIter;
        }
        else{
            return instance.iterator();
        }
    }

    @Override
    public void loadMyChunks$removeTicker(BlockEntity tickingBlockEntity) {
        tickableBlockEntities.remove(tickingBlockEntity);
        blockEntityList.remove(tickingBlockEntity);
    }

    @Unique
    public Level loadMyChunks$cast(){
        return Level.class.cast(this);
    }
    *///?}
}
