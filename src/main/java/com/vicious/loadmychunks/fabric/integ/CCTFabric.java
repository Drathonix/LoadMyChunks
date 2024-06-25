//? if fabric && cct {
package com.vicious.loadmychunks.fabric.integ;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.util.ModResource;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import dev.architectury.registry.registries.DeferredRegister;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.minecraft.resources.ResourceLocation;

public class CCTFabric {
    public static void init(){
        DeferredRegister<UpgradeType<? extends ITurtleUpgrade>> reg = DeferredRegister.create("loadmychunks", ITurtleUpgrade.typeRegistry());
        CCTRegistryContent.type = reg.register(ModResource.of("chunk_loader"),()->UpgradeType.simple(new TurtleChunkLoaderUpgrade()));
        reg.register();
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new LagometerPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel()), LMCContent.lagometerBlockEntity.get());
        PeripheralLookup.get().registerForBlockEntity((blockEntity, direction) -> new ChunkLoaderPeripheral(blockEntity.getBlockPos(),blockEntity.getLevel(), blockEntity.getChunkLoader()), LMCContent.chunkLoaderBlockEntity.get());
    }

    public static void clientInit(){
        CCTRegistryContent.registerClient();
    }
}
//?}
