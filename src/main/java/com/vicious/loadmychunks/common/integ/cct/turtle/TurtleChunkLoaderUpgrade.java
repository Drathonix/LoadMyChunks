//? if cct {
package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.registry.LMCContent;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
//? if >=1.20.6
/*import dan200.computercraft.api.upgrades.UpgradeType;*/
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TurtleChunkLoaderUpgrade implements ITurtleUpgrade {
    private final RegistrySupplier<Block> block;
    //? if >=1.20.6
    /*private RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>> upgradeType;*/

    public TurtleChunkLoaderUpgrade(RegistrySupplier<Block> block){
        this.block = block;
    }

    //? if >=1.20.6 {
    /*@Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return upgradeType.get();
    }
    *///?}


    //? if <1.20.6 {
    @Override
    public ResourceLocation getUpgradeID() {
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }
    @Override
    public TurtleUpgradeType getType() {
        return TurtleUpgradeType.PERIPHERAL;
    }
    @Override
    public String getUnlocalisedAdjective() {
        return "loadmychunks.turtle.adjective.loading";
    }
    //?}


    @Override
    public ItemStack getCraftingItem() {
        return block.get().asItem().getDefaultInstance();
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TurtleChunkLoaderPeripheral(turtle, side);
    }



    //? if >=1.20.6 {
    /*@Override
    public TurtleUpgradeType getUpgradeType() {
        return TurtleUpgradeType.PERIPHERAL;
    }

    public void setUpgradeType(RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>> upgradeType) {
        this.upgradeType = upgradeType;
    }
    @Override
    public Component getAdjective() {
        return Component.translatable("loadmychunks.turtle.adjective.loading");
    }
    *///?}
}
//?}
