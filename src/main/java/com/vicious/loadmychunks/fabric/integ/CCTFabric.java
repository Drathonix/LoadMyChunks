//? if fabric && cct {
package com.vicious.loadmychunks.fabric.integ;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.registry.FakeRegistrySupplier;
import com.vicious.loadmychunks.common.registry.LMCContent;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
//? if <1.20.4
/*import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;*/
//? if <1.20.6
/*import dan200.computercraft.api.upgrades.UpgradeSerialiser;*/
//? if >=1.20.6
import dan200.computercraft.api.upgrades.UpgradeType;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.minecraft.resources.ResourceLocation;

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
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new ChunkLoaderPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel(), blockEntity.getChunkLoader()), LMCContent.chunkLoaderBlockEntity.get());
    }
    //?}

    //? if <=1.20.4 {
    /*public static void init(){
        //? if >1.20.1
        DeferredRegister<UpgradeSerialiser<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(LoadMyChunks.MOD_ID,ITurtleUpgrade.serialiserRegistryKey());
        //? if <=1.20.1
        DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades = DeferredRegister.create(LoadMyChunks.MOD_ID,TurtleUpgradeSerialiser.registryId());
        LMCContent.chunkLoaderBlockMap.forEach((color,supplier)->{
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);
            //? if >=1.20.4
            RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>> reg = turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()->UpgradeSerialiser.simple((key)->tclu));
            //? if <=1.20.1
            RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>> reg = turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()-> TurtleUpgradeSerialiser.simple((key)->tclu));
            CCTRegistryContent.registrySuppliers.add(reg);
        });
        turtleUpgrades.register();

        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new LagometerPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel()), LMCContent.lagometerBlockEntity.get());
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new ChunkLoaderPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel(), blockEntity.getChunkLoader()), LMCContent.chunkLoaderBlockEntity.get());

    }
    *///?}

    public static void clientInit(){
        CCTRegistryContent.registerClient();
    }
}
//?}
