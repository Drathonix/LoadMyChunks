package com.vicious.loadmychunks.common.mixin.cct;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.mixin.MixinBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

//? if cct {
//? if >1.19.2
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
//? if <1.19.3
/*import dan200.computercraft.shared.computer.blocks.TileComputerBase;*/
//?}

//? if !cct {
/*@Mixin(LoadMyChunks.class)
public abstract class MixinAbstractComputerBlockEntity {
}*/
//?} else {
//? if >1.19.2
@Mixin(AbstractComputerBlockEntity.class)
//? if <1.19.3
/*@Mixin(TileComputerBase.class)*/
public abstract class MixinAbstractComputerBlockEntity extends MixinBlockEntity {
}
//?}
