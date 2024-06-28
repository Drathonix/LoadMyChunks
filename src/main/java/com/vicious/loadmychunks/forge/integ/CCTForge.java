//? if cct && forge {
/*package com.vicious.loadmychunks.forge.integ;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.registry.FakeRegistrySupplier;
import com.vicious.loadmychunks.common.registry.LMCContent;

import com.vicious.loadmychunks.common.util.ModResource;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
//? if >1.16.5 {
/^import dev.architectury.registry.registries.RegistrySupplier;
//? if <=1.20.1
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import dan200.computercraft.api.upgrades.UpgradeSerialiser;
^///?}
import dan200.computercraft.shared.Capabilities;
//? if <=1.16.5 {
import me.shedaniel.architectury.registry.RegistrySupplier;
import dan200.computercraft.ComputerCraft;
//?}
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CCTForge {
    //? if <1.20.6 && >=1.20.4
    /^public static DeferredRegister<UpgradeSerialiser<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(ITurtleUpgrade.serialiserRegistryKey(), LoadMyChunks.MOD_ID);^/
    //? if <=1.20.1 && >1.19.2
    /^public static DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades = DeferredRegister.create(TurtleUpgradeSerialiser.registryId(),LoadMyChunks.MOD_ID);^/
    //? if <=1.19.2 && >1.16.5
    /^public static DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades = DeferredRegister.create(TurtleUpgradeSerialiser.REGISTRY_ID,LoadMyChunks.MOD_ID);^/
    private static final ResourceLocation PERIPHERAL = new ResourceLocation("computercraft", "peripheral");

    @SubscribeEvent
    public static void registerCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if(event.getObject() instanceof BlockEntityChunkLoader) {
            BlockEntityChunkLoader becl = (BlockEntityChunkLoader) event.getObject();
            event.addCapability(PERIPHERAL, new ICapabilityProvider() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                    return Capabilities.CAPABILITY_PERIPHERAL.orEmpty(capability,LazyOptional.of(()->new ChunkLoaderPeripheral(becl.getBlockPos(),becl.getLevel(), becl.getChunkLoader())));
                }
            });
        }
        if(event.getObject() instanceof BlockEntityLagometer) {
            BlockEntityLagometer bel = (BlockEntityLagometer) event.getObject();
            event.addCapability(PERIPHERAL, new ICapabilityProvider() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                    return Capabilities.CAPABILITY_PERIPHERAL.orEmpty(capability,LazyOptional.of(()->new LagometerPeripheral(bel.getBlockPos(),bel.getLevel())));
                }
            });
        }
    }


    public static void init(IEventBus meb){
        LMCContent.chunkLoaderBlockMap.forEach((color, supplier)->{
            //? if >1.16.5
            /^TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier);^/
            //? if <=1.16.5
            TurtleChunkLoaderUpgrade tclu = new TurtleChunkLoaderUpgrade(supplier, ModResource.of((!color.isEmpty() ? color + "_" : "") + "chunk_loader"));

            //? if >=1.20.4
            /^RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>> reg = new FakeRegistrySupplier<>(turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()-> UpgradeSerialiser.simple((key)->tclu)));^/
            //? if >1.16.5 && <1.20.4
            /^RegistrySupplier<TurtleUpgradeSerialiser<?>> reg = new FakeRegistrySupplier<>(turtleUpgrades.register((!color.isEmpty() ? color + "_" : "") + "chunk_loader", ()-> TurtleUpgradeSerialiser.simple((key)->tclu)));^/
            //? <=1.16.5 {
            RegistrySupplier<ITurtleUpgrade> reg = new FakeRegistrySupplier<>(tclu);
            ComputerCraftAPI.registerTurtleUpgrade(tclu);
            //?}
            CCTRegistryContent.registrySuppliers.add(reg);
        });
        //? >=1.18.2
        /^turtleUpgrades.register(meb);^/
        MinecraftForge.EVENT_BUS.register(CCTForge.class);
    }

    public static void clientInit(){
        CCTRegistryContent.registerClient();
    }
}
*///?}
