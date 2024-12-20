package com.vicious.loadmychunks.common.mixin.cct;

import com.vicious.loadmychunks.common.mixin.MixinBlockEntity;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractComputerBlockEntity.class)
public abstract class MixinAbstractComputerBlockEntity extends MixinBlockEntity {
}
