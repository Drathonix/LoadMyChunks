//? if neoforge && cct {
/*package com.vicious.loadmychunks.neoforge.integ;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.integ.Integrations;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.registry.FakeRegistrySupplier;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.neoforge.LMCNeoInit;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
//? if <1.20.6
import dan200.computercraft.api.upgrades.UpgradeSerialiser;
//? if >=1.20.6
/^import dan200.computercraft.api.upgrades.UpgradeType;^/
import dan200.computercraft.shared.ModRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCTNeo {
    //? if >=1.20.6 {
    /^public static DeferredRegister<UpgradeType<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(ITurtleUpgrade.typeRegistry(),LoadMyChunks.MOD_ID);
    static {
        LMCContent.chunkLoaderBlockMap.forEach((color,supplier)->{
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);
            RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>> reg = new FakeRegistrySupplier<>(turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()->UpgradeType.simple(tclu)));
            tclu.setUpgradeType(reg);
            CCTRegistryContent.registrySuppliers.add(reg);
        });
    }
    ^///?}
    //? if <1.20.6 {
    public static DeferredRegister<UpgradeSerialiser<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(ITurtleUpgrade.serialiserRegistryKey(),LoadMyChunks.MOD_ID);
    static {
        LMCContent.chunkLoaderBlockMap.forEach((color,supplier)->{
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);
            RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>> reg = new FakeRegistrySupplier<>(turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()->UpgradeSerialiser.simple((key)->tclu)));
            CCTRegistryContent.registrySuppliers.add(reg);
        });
    }
    //?}

    public static void register(IEventBus bus) {
        turtleUpgrades.register(bus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        Block[] blocks = new Block[LMCContent.chunkLoaderBlockMap.size()];
        int i = 0;
        for (RegistrySupplier<Block> chunkLoaderBlock : LMCContent.chunkLoaderBlockMap.values()) {
            blocks[i]=chunkLoaderBlock.get();
            i++;
        }
        event.registerBlock(PeripheralCapability.get(), (level, pos, state, be, side) -> {
            if (be instanceof BlockEntityChunkLoader becl) {
                return new ChunkLoaderPeripheral(becl.getBlockPos(),becl.getLevel(), becl.getChunkLoader());
            }
            return null;
        },blocks);
        event.registerBlock(PeripheralCapability.get(), (level, pos, state, be, side) -> {
            if (be instanceof BlockEntityLagometer lagometer) {
                return new LagometerPeripheral(lagometer.getBlockPos(),lagometer.getLevel());
            }
            return null;
        },LMCContent.lagometerBlock.get());
    }


    public static void init(IEventBus bus){
        bus.addListener(CCTNeo::registerCapabilities);
        register(bus);
    }

    public static void clientInit(){
        CCTRegistryContent.registerClient();
    }
}
*///?}
