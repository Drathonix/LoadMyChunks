package com.vicious.loadmychunks.mixins;

import com.vicious.loadmychunks.bridge.IChunkMapMixin;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.TickingTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public class MixinChunkMap implements IChunkMapMixin {
    @Shadow @Final private Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap;

    @Override
    public Long2ObjectLinkedOpenHashMap<ChunkHolder> loadMyChunks$getUpdatingChunkMap() {
        return updatingChunkMap;
    }
}
