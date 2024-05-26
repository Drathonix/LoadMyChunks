package com.vicious.loadmychunks.mixins;

import com.vicious.loadmychunks.bridge.IDestroyable;
import com.vicious.loadmychunks.bridge.ILevelChunkMixin;
import com.vicious.loadmychunks.bridge.ILevelMixin;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(LevelChunk.class)

public abstract class MixinLevelChunk implements ILevelChunkMixin {
    @Shadow @Final Level level;
    //1.16.5 specific.
    @Unique @Final private Map<BlockPos, BlockEntity> loadmychunks$tickersInLevel = new HashMap<>();

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
        boolean flag = loadMyChunks$loadDataModule.shouldUseTimings() && !level.isClientSide;
        if(flag || loadMyChunks$loadDataModule.timeRegardless){
            loadMyChunks$loadDataModule.getTickTimer().start();
        }
        Iterator<BlockEntity> iterator = loadmychunks$tickersInLevel.values().iterator();
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
        BlockEntity blockEntity = getBlockEntity(blockPos);
        if(blockEntity instanceof IDestroyable){
            ((IDestroyable)blockEntity).destroy();
        }
        loadmychunks$tickersInLevel.remove(blockPos);
    }

    @Inject(method = "setBlockEntity",at = @At(value = "INVOKE",target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",shift = At.Shift.AFTER))
    public void addTicker(BlockPos blockPos, BlockEntity blockEntity, CallbackInfo ci){
        if(blockEntity instanceof TickableBlockEntity) {
            loadmychunks$tickersInLevel.put(blockPos, blockEntity);
        }
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
