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
import dan200.computercraft.api.upgrades.UpgradeType;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCTNeo {
    public static DeferredRegister<UpgradeType<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(ITurtleUpgrade.typeRegistry(),LoadMyChunks.MOD_ID);
    static {
        CCTRegistryContent.type = new FakeRegistrySupplier<>(turtleUpgrades.register("chunk_loader", ()->UpgradeType.simple(new TurtleChunkLoaderUpgrade())));
    }

    public static void register(IEventBus bus) {
        turtleUpgrades.register(bus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        Block[] blocks = new Block[LMCContent.chunkLoaderBlocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> chunkLoaderBlock : LMCContent.chunkLoaderBlocks) {
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
