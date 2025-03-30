package com.drathonix.loadmychunks.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity {
    @Shadow @Nullable
    public abstract Level getLevel();

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow @Nullable protected Level level;
}
