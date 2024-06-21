package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.block.BlockEntityChunkLoader;
import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.integ.Integrations;
import com.vicious.loadmychunks.integ.cct.ChunkLoaderPeripheral;
import com.vicious.loadmychunks.util.EnumArgument;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;


@Mod(LoadMyChunks.MOD_ID)
public class LoadMyChunksNeoForge {
    public LoadMyChunksNeoForge(IEventBus meb) {
        //TODO fix?
        //EventBus.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        NeoForge.EVENT_BUS.register(this);
        meb.addListener(this::registerCapabilities);
        DeferredRegister<ArgumentTypeInfo<?,?>> infos = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE,LoadMyChunks.MOD_ID);
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(EnumArgument.class,new EnumArgument.Info());
        infos.register("lmcenum",()-> info);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        Integrations.whenModLoaded("computercraft",()->{
            Block[] blocks = new Block[LoadMyChunks.chunkLoaderBlocks.size()];
            int i = 0;
            for (RegistrySupplier<Block> chunkLoaderBlock : LoadMyChunks.chunkLoaderBlocks) {
                blocks[i]=chunkLoaderBlock.get();
                i++;
            }
            event.registerBlock(PeripheralCapability.get(), (level, pos, state, be, side) -> {
                if (be instanceof BlockEntityChunkLoader becl) {
                    return new ChunkLoaderPeripheral(becl.getBlockPos(),becl.getLevel(), becl.getChunkLoader());
                }
                return null;
            },blocks);
        });
    }

    @Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus=Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
    public static class CMEs {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
            LoadMyChunksClient.init();
        }
    }



    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public void serverStopped(ServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }
}