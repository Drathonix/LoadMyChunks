package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public abstract class MixinChunkMap implements IChunkMapMixin {
    @Shadow @Final private Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap;

    @Shadow abstract boolean anyPlayerCloseEnoughForSpawning(ChunkPos chunkPos);

    @Override
    public Long2ObjectLinkedOpenHashMap<ChunkHolder> lmc$getUpdatingChunkMap() {
        return updatingChunkMap;
    }

    @Override
    public boolean lmc$playerDistCheck(ChunkPos pos) {
        return anyPlayerCloseEnoughForSpawning(pos);
    }
}
