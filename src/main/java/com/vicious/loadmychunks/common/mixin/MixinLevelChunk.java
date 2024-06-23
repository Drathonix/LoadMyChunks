package com.vicious.loadmychunks.common.mixin;

import com.vicious.loadmychunks.common.bridge.IDestroyable;
import com.vicious.loadmychunks.common.bridge.ILevelChunkMixin;
import com.vicious.loadmychunks.common.bridge.ILevelMixin;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
//? if >1.16.5 {
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
//?}
//? if <=1.16.5 {
/*import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
*///?}
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
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
import java.util.function.Consumer;

@Mixin(LevelChunk.class)

public abstract class MixinLevelChunk
    //? if >1.16.5
        extends MixinChunkAccess
        implements ILevelChunkMixin {
    @Shadow @Final Level level;

    @Override
    public ChunkDataModule loadMyChunks$getDataModule() {
        return loadMyChunks$loadDataModule;
    }

    @Override
    public long loadMyChunks$posAsLong() {
        return chunkPos.toLong();
    }

    //? if >1.16.5 {
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$queuedTickers = new ArrayList<>();
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$tickers = new ArrayList<>();


    @Shadow @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos arg);

    @Unique private ChunkDataModule loadMyChunks$loadDataModule;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;Lnet/minecraft/world/level/levelgen/blending/BlendingData;)V",at = @At("RETURN"))
    public void setup(Level arg, ChunkPos arg2, UpgradeData arg3, LevelChunkTicks arg4, LevelChunkTicks arg5, long l, LevelChunkSection[] args, LevelChunk.PostLoadProcessor arg6, BlendingData arg7, CallbackInfo ci){
        if(level instanceof ServerLevel sl) {
            this.loadMyChunks$loadDataModule = ChunkDataManager.getOrCreateChunkData(sl,chunkPos);
            this.loadMyChunks$loadDataModule.assignChunk(this);
        }
    }

    @Override
    public void loadMyChunks$tick() {
        boolean applyTimings = loadMyChunks$loadDataModule.shouldApplyTimings() && !level.isClientSide;
        boolean useTimings = applyTimings || (!level.isClientSide && loadMyChunks$loadDataModule.shouldUseTimings());
        Iterator<TickingBlockEntity> iterator = loadMyChunks$queuedTickers.iterator();
        // 1.0.3 Conmod patch
        while(iterator.hasNext()){
            TickingBlockEntity tickingblockentity = iterator.next();
            loadMyChunks$tickers.add(tickingblockentity);
            iterator.remove();
        }
        if(useTimings){
            loadMyChunks$loadDataModule.getTickTimer().start();
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
        if(useTimings){
            loadMyChunks$loadDataModule.getTickTimer().end();
            loadMyChunks$loadDataModule.inform();
            if(applyTimings && loadMyChunks$loadDataModule.isOverticked()){
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

    //?}

    //TODO: Remove redundant code. For now I'm just assuming 1.16.5 is too complex to really integrate well (I'm definitely wrong)
    //? if <=1.16.5 {

    /*@Unique private final List<BlockEntity> loadMyChunks$queued = new ArrayList<>();
    @Unique private final List<BlockEntity> loadMyChunks$tickers = new ArrayList<>();

    @Shadow @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos arg);

    @Shadow @Final private ChunkPos chunkPos;

    @Shadow public abstract BlockState getBlockState(BlockPos blockPos);

    @Shadow @Nullable public abstract BlockEntity getBlockEntity(BlockPos blockPos, LevelChunk.EntityCreationType entityCreationType);

    @Unique private ChunkDataModule loadMyChunks$loadDataModule;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V",at = @At("RETURN"))
    public void setup(Level level, ChunkPos chunkPos, ChunkBiomeContainer chunkBiomeContainer, UpgradeData upgradeData, TickList tickList, TickList tickList2, long l, LevelChunkSection[] levelChunkSections, Consumer consumer, CallbackInfo ci){
        if(level instanceof ServerLevel) {
            this.loadMyChunks$loadDataModule = ChunkDataManager.getOrCreateChunkData((ServerLevel)level,chunkPos);
            this.loadMyChunks$loadDataModule.assignChunk(this);
        }
    }

    @Override
    public void loadMyChunks$tick(ProfilerFiller profilerFiller) {
        Iterator<BlockEntity> iterator = loadMyChunks$queued.iterator();
        while(iterator.hasNext()){
            loadMyChunks$tickers.add(iterator.next());
            iterator.remove();
        }

        boolean applyTimings = loadMyChunks$loadDataModule.shouldApplyTimings() && !level.isClientSide;
        boolean useTimings = applyTimings || (!level.isClientSide && loadMyChunks$getDataModule().shouldUseTimings());
        if(useTimings){
            loadMyChunks$loadDataModule.getTickTimer().start();
        }
        iterator = loadMyChunks$tickers.iterator();
        while(iterator.hasNext()){
            BlockEntity tickingblockentity = iterator.next();

            if (tickingblockentity.isRemoved()) {
                ((ILevelMixin)level).loadMyChunks$removeTicker(tickingblockentity);
                iterator.remove();
            } else if(tickingblockentity instanceof TickableBlockEntity){
                try {
                    profilerFiller.push(() -> String.valueOf(BlockEntityType.getKey(tickingblockentity.getType())));
                    if (tickingblockentity.getType().isValid(getBlockState(tickingblockentity.getBlockPos()).getBlock())) {
                        ((TickableBlockEntity)tickingblockentity).tick();
                    } else {
                        tickingblockentity.logInvalidState();
                    }
                    profilerFiller.pop();
                } catch (Throwable var8) {
                    CrashReport crashReport = CrashReport.forThrowable(var8, "Ticking block entity");
                    CrashReportCategory crashReportCategory = crashReport.addCategory("Block entity being ticked");
                    tickingblockentity.fillCrashReportCategory(crashReportCategory);
                    throw new ReportedException(crashReport);
                }
            }
        }
        if(useTimings){
            loadMyChunks$loadDataModule.getTickTimer().end();
            loadMyChunks$loadDataModule.inform();
            if(applyTimings && loadMyChunks$loadDataModule.isOverticked()){
                loadMyChunks$loadDataModule.startShutoff();
                ChunkDataManager.markShutDown((ServerLevel)level,chunkPos);
            }
        }
    }

    @Inject(method = "removeBlockEntity",at = @At(value = "HEAD"))
    public void properlyDestroyTileEntities(BlockPos blockPos, CallbackInfo ci){
        BlockEntity blockEntity = getBlockEntity(blockPos);
        if(blockEntity instanceof IDestroyable){
            ((IDestroyable)blockEntity).destroy();
        }
        loadMyChunks$tickers.remove(blockEntity);
        loadMyChunks$queued.remove(blockEntity);
    }

    @Inject(method = "setBlockEntity",at = @At(value = "INVOKE",target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",shift = At.Shift.AFTER))
    public void addTicker(BlockPos blockPos, BlockEntity blockEntity, CallbackInfo ci){
        if(blockEntity instanceof TickableBlockEntity) {
            loadMyChunks$queued.add(blockEntity);
        }
    }
    *///?}
}
