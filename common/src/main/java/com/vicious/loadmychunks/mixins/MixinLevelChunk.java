package com.vicious.loadmychunks.mixins;

import com.vicious.loadmychunks.bridge.IDestroyable;
import com.vicious.loadmychunks.bridge.ILevelChunkMixin;
import com.vicious.loadmychunks.bridge.ILevelMixin;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(LevelChunk.class)

public abstract class MixinLevelChunk extends MixinChunkAccess implements ILevelChunkMixin {
    @Shadow @Final Level level;
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$queuedTickers = new ArrayList<>();
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$tickers = new ArrayList<>();


    @Shadow @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos arg);

    @Unique private ChunkDataModule loadMyChunks$loadDataModule;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;Lnet/minecraft/world/level/levelgen/blending/BlendingData;)V",at = @At("RETURN"))
    public void setup(Level level, ChunkPos chunkPos, UpgradeData upgradeData, LevelChunkTicks levelChunkTicks, LevelChunkTicks levelChunkTicks2, long l, LevelChunkSection[] levelChunkSections, LevelChunk.PostLoadProcessor postLoadProcessor, BlendingData blendingData, CallbackInfo ci){
        if(level instanceof ServerLevel sl) {
            this.loadMyChunks$loadDataModule = ChunkDataManager.getOrCreateChunkData(sl,chunkPos);
            this.loadMyChunks$loadDataModule.assignChunk(this);
        }
    }

    @Override
    public void loadMyChunks$tick() {
        boolean flag = loadMyChunks$loadDataModule.shouldUseTimings() && !level.isClientSide;
        if(flag || loadMyChunks$loadDataModule.timeRegardless){
            loadMyChunks$loadDataModule.getTickTimer().start();
        }
        Iterator<TickingBlockEntity> iterator = loadMyChunks$queuedTickers.iterator();
        // 1.0.3 Conmod patch
        while(iterator.hasNext()){
            TickingBlockEntity tickingblockentity = iterator.next();
            loadMyChunks$tickers.add(tickingblockentity);
            iterator.remove();
        }
        iterator = loadMyChunks$tickers.iterator();
        // patch end
        while(iterator.hasNext()){
            TickingBlockEntity tickingblockentity = iterator.next();
            if (tickingblockentity.isRemoved()) {
                ((ILevelMixin)level).loadMyChunks$removeTicker(tickingblockentity);
                iterator.remove();
            } else {
                tickingblockentity.tick();
            }
        }
        if(flag || loadMyChunks$loadDataModule.timeRegardless){
            loadMyChunks$loadDataModule.timeRegardless=false;
            loadMyChunks$loadDataModule.getTickTimer().end();
            loadMyChunks$loadDataModule.inform();
        }

        if(flag){
            if(loadMyChunks$loadDataModule.isOverticked()){
                loadMyChunks$loadDataModule.startShutoff();
                ChunkDataManager.markShutDown((ServerLevel)level,chunkPos);
            }
        }
    }

    @Inject(method = "removeBlockEntity",at = @At(value = "HEAD"))
    public void properlyDestroyTileEntities(BlockPos blockPos, CallbackInfo ci){
        if(getBlockEntity(blockPos) instanceof IDestroyable destroyable){
            destroyable.destroy();
        }
    }

    @Inject(method = "clearAllBlockEntities",at = @At("RETURN"))
    public void clearQueues(CallbackInfo ci){
        loadMyChunks$queuedTickers.clear();
        loadMyChunks$tickers.clear();
    }

    @Inject(method = "createTicker",at = @At("RETURN"))
    public <T extends BlockEntity> void addToQueue(T blockEntity, BlockEntityTicker<T> blockEntityTicker, CallbackInfoReturnable<TickingBlockEntity> cir){
        loadMyChunks$queuedTickers.add(cir.getReturnValue());
    }

    @Override
    public ChunkDataModule loadMyChunks$getDataModule() {
        return loadMyChunks$loadDataModule;
    }

    @Override
    public long loadMyChunks$posAsLong() {
        return chunkPos.toLong();
    }
}
