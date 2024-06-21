package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.util.BoolArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;


@Mod(LoadMyChunks.MOD_ID)
public class LoadMyChunksNeoForge {
    public LoadMyChunksNeoForge(IEventBus modBus) {
        LoadMyChunks.init();
        NeoForge.EVENT_BUS.register(this);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE,LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(modBus);
        ForgeLoaderTypes.init();;
    }

    @EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus=EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
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