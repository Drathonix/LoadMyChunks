package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.*;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;

import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import com.drathonix.loadmychunks.common.util.ProtectedEntityTickList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.Consumer;
import net.minecraft.world.level.chunk.*;

//? if >1.16.5 {
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
//?} else {
/*import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.server.level.ChunkHolder;
*///?}

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(LevelChunk.class)

public abstract class MixinLevelChunk
    //? if >1.16.5 {
        extends MixinChunkAccess
    //?}
        implements ILevelChunkMixin {
    @Shadow
    @Final
    Level level;

    @Unique private ChunkDataModule loadMyChunks$loadDataModule;

    @Override
    public ChunkDataModule loadMyChunks$getDataModule() {
        if (this.loadMyChunks$loadDataModule == null || this.loadMyChunks$loadDataModule.isInvalid()) {
            this.loadMyChunks$loadDataModule = ChunkDataManager.getOrCreateChunkData((ServerLevel) level, loadMyChunks$posAsLong());
        }
        return this.loadMyChunks$loadDataModule;
    }

    @Override
    public long loadMyChunks$posAsLong() {
        return chunkPos.toLong();
    }

    //? if <1.18.2 {
    /*@Inject(method = "addEntity",at = @At("TAIL"))
    public void onAdd(Entity entity, CallbackInfo ci){
        if(level instanceof ServerLevel){
            loadMyChunks$getDataModule().lmc$addEntity(entity);
        }
    }
    @Inject(method = "removeEntity(Lnet/minecraft/world/entity/Entity;I)V",at = @At("TAIL"))
    public void onRemove(Entity entity, int i, CallbackInfo ci){
        if(level instanceof ServerLevel){
            loadMyChunks$getDataModule().lmc$removeEntity(entity);
        }
    }
    @Override
    public void loadMyChunks$1165reloadEntities(){
        if(level instanceof ServerLevel) {
            for (ClassInstanceMultiMap<Entity> entitySection : entitySections) {
                for (Entity ent : entitySection) {
                    loadMyChunks$getDataModule().lmc$addEntity(ent);
                }
            }
        }
    }
    *///?}

    @Unique
    @Override
    public void loadMyChunks$tickEntities(ProfilerFiller profilerfiller) {
        if(level instanceof ServerLevel) {
            loadMyChunks$getDataModule().tickEntities((ServerLevel) level,profilerfiller);
        }
    }


    //? if >1.16.5 {
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$queuedTickers = new ArrayList<>();
    @Unique
    private final List<TickingBlockEntity> loadMyChunks$tickers = new ArrayList<>();

    @Shadow @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos arg);

    @Shadow public abstract Level getLevel();

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;Lnet/minecraft/world/level/levelgen/blending/BlendingData;)V",at = @At("RETURN"))
    public void setup(Level arg, ChunkPos arg2, UpgradeData arg3, LevelChunkTicks arg4, LevelChunkTicks arg5, long l, LevelChunkSection[] args, LevelChunk.PostLoadProcessor arg6, BlendingData arg7, CallbackInfo ci){
        if(level instanceof ServerLevel) {
            loadMyChunks$getDataModule();
        }
    }

    @Override
    public void loadMyChunks$tick() {
        ChunkDataModule cdm = loadMyChunks$getDataModule();
        if(level instanceof ServerLevel) {
            cdm.preTick((ServerLevel) level);
        }
        boolean applyTimings = cdm.shouldApplyTimings() && !level.isClientSide;
        boolean useTimings = applyTimings || (!level.isClientSide && cdm.shouldUseTimings());
        Iterator<TickingBlockEntity> iterator = loadMyChunks$queuedTickers.iterator();
        // 1.0.3 Conmod patch
        while(iterator.hasNext()){
            TickingBlockEntity tickingblockentity = iterator.next();
            loadMyChunks$tickers.add(tickingblockentity);
            iterator.remove();
        }
        if(useTimings){
            cdm.getTickTimer().startBlockEntities();
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
            cdm.getTickTimer().endBlockEntities();
            cdm.inform();
            if(applyTimings && cdm.isOverticked()){
                ILoadState prev = cdm.getLoadState();
                cdm.startShutoff();
                ChunkDataManager.markShutDown((ServerLevel)level,chunkPos,prev);
            }
        }
    }

    // Use inject instead due to conflict with fabric mixins
    @Inject(method = "getBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk$EntityCreationType;)Lnet/minecraft/world/level/block/entity/BlockEntity;",at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0),
            slice = @Slice(
            from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;isRemoved()Z"
            )),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void properlyDestroyTileEntities1(BlockPos blockPos, LevelChunk.EntityCreationType entityCreationType, CallbackInfoReturnable<BlockEntity> cir, BlockEntity blockEntity){
        if(!level.isClientSide){
            if(blockEntity instanceof IDestroyable destroyable){
                destroyable.loadMyChunks$destroy();
            }
        }
    }

    @Redirect(method = "removeBlockEntity",at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0))
    public Object properlyDestroyTileEntities2(Map<?,?> instance, Object o){
        Object rem = instance.remove(o);
        if(rem instanceof IDestroyable destroyable && !level.isClientSide()){
            destroyable.loadMyChunks$destroy();
        }
        return rem;
    }

    @Inject(method = "clearAllBlockEntities",at = @At("RETURN"))
    public void clearQueues(CallbackInfo ci){
        loadMyChunks$queuedTickers.clear();
        loadMyChunks$tickers.clear();
    }

    //Use lazy typing to avoid needing an AT
    @Redirect(method = "updateBlockEntityTicker",at = @At(value = "INVOKE",target = "Ljava/util/Map;compute(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;"))
    public <K,V> Object addToQueue(Map<K,V> instance, K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction){
        //Ensure this will be the first instance
        if(!instance.containsKey(key)){
            Object current = instance.get(key);
            TickingBlockEntity result = (TickingBlockEntity)instance.compute(key, remappingFunction);
            if(result != null) {
                loadMyChunks$queuedTickers.add(result);
            }
            //Account for the new ticker being invalidated (somehow)
            //TODO: figure out what I was thinking here (I don't think this actually does anything)
            else if(current != null){
                loadMyChunks$tickers.remove(result);
                loadMyChunks$queuedTickers.remove(result);
            }
            return result;
        }
        return remappingFunction.apply(key, instance.get(key));
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

    @Shadow
    @Final
    private ClassInstanceMultiMap<Entity>[] entitySections;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V",at = @At("RETURN"))
    public void setup(Level level, ChunkPos chunkPos, ChunkBiomeContainer chunkBiomeContainer, UpgradeData upgradeData, TickList tickList, TickList tickList2, long l, LevelChunkSection[] levelChunkSections, Consumer consumer, CallbackInfo ci){
        if(level instanceof ServerLevel) {
            loadMyChunks$getDataModule();
        }
    }

    @Override
    public void loadMyChunks$tick(ProfilerFiller profilerFiller) {
        ChunkDataModule cdm = loadMyChunks$getDataModule();
        if(level instanceof ServerLevel) {
            cdm.preTick((ServerLevel) level);
        }
        Iterator<BlockEntity> iterator = loadMyChunks$queued.iterator();
        while(iterator.hasNext()){
            loadMyChunks$tickers.add(iterator.next());
            iterator.remove();
        }

        boolean applyTimings = cdm.shouldApplyTimings() && !level.isClientSide;
        boolean useTimings = applyTimings || (!level.isClientSide && loadMyChunks$getDataModule().shouldUseTimings());
        if(useTimings){
            cdm.getTickTimer().startBlockEntities();
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
            cdm.getTickTimer().endBlockEntities();
            cdm.inform();
            if(applyTimings && cdm.isOverticked()){
                cdm.consumeLoadState(prev->{
                    cdm.startShutoff();
                    ChunkDataManager.markShutDown((ServerLevel)level,chunkPos,prev);
                });
            }
        }
    }

    // Use inject instead due to conflict with fabric mixins
    @Inject(method = "getBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk$EntityCreationType;)Lnet/minecraft/world/level/block/entity/BlockEntity;",at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0),
            slice = @Slice(
            from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;isRemoved()Z"
            )),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void properlyDestroyTileEntities1(BlockPos blockPos, LevelChunk.EntityCreationType entityCreationType, CallbackInfoReturnable<BlockEntity> cir, BlockEntity blockEntity){
        if(!level.isClientSide){
            if(blockEntity instanceof IDestroyable){
                ((IDestroyable)blockEntity).loadMyChunks$destroy();
            }
        }
    }
    @Redirect(method = "removeBlockEntity",at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0))
    public Object properlyDestroyTileEntities2(Map<?,?> instance, Object o){
        Object rem = instance.remove(o);
        if(rem instanceof IDestroyable && !level.isClientSide()){
            ((IDestroyable) rem).loadMyChunks$destroy();
        }
        return rem;
    }

    @Inject(method = "setBlockEntity",at = @At(value = "INVOKE",target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",shift = At.Shift.AFTER))
    public void addTicker(BlockPos blockPos, BlockEntity blockEntity, CallbackInfo ci){
        if(blockEntity instanceof TickableBlockEntity) {
            if(!level.isClientSide()) {
                loadMyChunks$queued.add(blockEntity);
            }
        }
    }
    *///?}
}
