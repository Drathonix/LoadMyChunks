//? if cct {
/*package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.registry.LMCContent;
//? if <=1.18.2
/^import dan200.computercraft.api.client.TransformedModel;^/
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
//? if >=1.20.6
/^import dan200.computercraft.api.upgrades.UpgradeType;^/
//? if >1.16.5
import dev.architectury.registry.registries.RegistrySupplier;
//? if <=1.16.5
/^import me.shedaniel.architectury.registry.RegistrySupplier;^/

//? if >1.19.2
import net.minecraft.core.registries.BuiltInRegistries;
//? if <=1.19.2
/^import net.minecraft.core.Registry;^/
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TurtleChunkLoaderUpgrade implements ITurtleUpgrade {
    private final RegistrySupplier<Block> block;
    //? if >=1.20.6
    /^private RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>> upgradeType;^/

    //? if >1.16.5 {
    public TurtleChunkLoaderUpgrade(RegistrySupplier<Block> block){
        this.block = block;
    }
    //?}

    //? if <=1.16.5 {
    /^private final ResourceLocation key;
    public TurtleChunkLoaderUpgrade(RegistrySupplier<Block> block, ResourceLocation key
    ){
        this.block = block;
        this.key = key;
    }
    ^///?}

    //? if >=1.20.6 {
    /^@Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return upgradeType.get();
    }
    ^///?}


    //? if <1.20.6 {
    @Override
    public ResourceLocation getUpgradeID() {
        //? if >1.19.2
        return BuiltInRegistries.BLOCK.getKey(block.get());
        //? if <=1.19.2 && >1.16.5
        /^return Registry.BLOCK.getKey(block.get());^/
        //? if <=1.16.5
        /^return key;^/
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

    //? if <=1.18.2 {
    /^@NotNull
    @Override
    public TransformedModel getModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side) {
        return TransformedModel.of(getCraftingItem(),side == TurtleSide.LEFT ? UpgradeModeller.leftTransform : UpgradeModeller.rightTransform);
    }
    ^///?}


    //? if >=1.20.6 {
    /^@Override
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
    ^///?}
}
*///?}
