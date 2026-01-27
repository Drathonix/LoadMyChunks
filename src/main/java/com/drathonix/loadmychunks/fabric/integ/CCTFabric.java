//? if fabric && computercraft {
/*package com.drathonix.loadmychunks.fabric.integ;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.drathonix.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.drathonix.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.drathonix.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.drathonix.loadmychunks.common.registry.LMCContent;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
//? if <1.20.4 {
/^import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
^///?} else if <1.20.6 {
/^import dan200.computercraft.api.upgrades.UpgradeSerialiser;
^///?} else {
import dan200.computercraft.api.upgrades.UpgradeType;
//?}
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class CCTFabric {
    //? if >=1.20.6 {
    public static void init(){
        DeferredRegister<UpgradeType<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create("loadmychunks", ITurtleUpgrade.typeRegistry());
        LMCContent.chunkLoaderBlockMap.forEach((color,supplier)->{
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);
            RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>> reg = turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()->UpgradeType.simple(tclu));
            tclu.setUpgradeType(reg);
            CCTRegistryContent.registrySuppliers.add(reg);
        });
        turtleUpgrades.register();
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new LagometerPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel()), LMCContent.lagometerBlockEntity.get());
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new ChunkLoaderPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel(), blockEntity.loadMyChunks$getChunkLoader()), LMCContent.chunkLoaderBlockEntity.get());
        CCTRegistryContent.register();
    }
    //?} else {
    /^public static void init(){
        //? if >1.20.1 {
        DeferredRegister<UpgradeSerialiser<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(LoadMyChunks.MOD_ID,ITurtleUpgrade.serialiserRegistryKey());
        //?} else {
        /^¹DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades = DeferredRegister.create(LoadMyChunks.MOD_ID,TurtleUpgradeSerialiser.registryId());
        ¹^///?}
        LMCContent.chunkLoaderBlockMap.forEach((color,supplier)->{
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);
            //? if >=1.20.4 {
            RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>> reg = turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()->UpgradeSerialiser.simple((key)->tclu));
            //?} else {
            /^¹RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>> reg = turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()-> TurtleUpgradeSerialiser.simple((key)->tclu));
            ¹^///?}
            CCTRegistryContent.registrySuppliers.add(reg);
        });
        turtleUpgrades.register();

        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new LagometerPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel()), LMCContent.lagometerBlockEntity.get());
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new ChunkLoaderPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel(), blockEntity.loadMyChunks$getChunkLoader()), LMCContent.chunkLoaderBlockEntity.get());
        CCTRegistryContent.register();
    }
    ^///?}

    public static void clientInit(){
        CCTRegistryContent.registerClient();
    }
}
*///?}
