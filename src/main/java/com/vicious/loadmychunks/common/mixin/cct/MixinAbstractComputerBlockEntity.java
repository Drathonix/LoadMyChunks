package com.vicious.loadmychunks.common.mixin.cct;
//? if cct {
import com.vicious.loadmychunks.common.mixin.MixinBlockEntity;
import dan200.computercraft.core.computer.Computer;
//? if >1.19.2
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
//? if <1.19.3
/*import dan200.computercraft.shared.computer.blocks.TileComputerBase;*/
import org.spongepowered.asm.mixin.Mixin;

//? if >1.19.2
@Mixin(AbstractComputerBlockEntity.class)
//? if <1.19.3
/*@Mixin(TileComputerBase.class)*/
public abstract class MixinAbstractComputerBlockEntity extends MixinBlockEntity {
}
//?}
