package com.vicious.loadmychunks.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ChunkAccess.class)
public
//? if >1.16.5
/*class*/
//? if <=1.16.5
interface
MixinChunkAccess {
    //? if >1.16.5 {
    /*@Shadow @Final protected Map<BlockPos, BlockEntity> blockEntities;
    @Shadow @Final protected ChunkPos chunkPos;
    *///?}
}