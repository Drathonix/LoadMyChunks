//? if neoforge {
/*package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.integ.Integrations;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.peripheral.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.common.integ.cct.peripheral.LagometerPeripheral;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.util.BoolArgument;
//? if cct
import com.vicious.loadmychunks.neoforge.integ.CCTNeo;
import dan200.computercraft.api.peripheral.PeripheralCapability;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LMCNeoInit {
    public static void init(IEventBus meb) {
        NeoForge.EVENT_BUS.register(LMCNeoInit.class);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(meb);
        meb.addListener(LMCNeoInit::registerCapabilities);
        //? if cct
        CCTNeo.register(meb);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        //? if cct {
        Integrations.whenModLoaded("computercraft",()->{
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
        });
        //?}
    }


    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }

    public static void clientInit() {
        //? if cct
        CCTRegistryContent.registerClient();
    }
}
*///?}
