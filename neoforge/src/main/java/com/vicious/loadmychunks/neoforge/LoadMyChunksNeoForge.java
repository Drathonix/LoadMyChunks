package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.util.EnumArgument;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


@Mod(LoadMyChunks.MOD_ID)
public class LoadMyChunksNeoForge {
    public LoadMyChunksNeoForge() {
        //TODO fix?
        //EventBus.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        NeoForge.EVENT_BUS.register(this);
        DeferredRegister<ArgumentTypeInfo<?,?>> infos = DeferredRegister.create(LoadMyChunks.MOD_ID,Registries.COMMAND_ARGUMENT_TYPE);
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(EnumArgument.class,new EnumArgument.Info());
        infos.register("lmcenum",()-> info);
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