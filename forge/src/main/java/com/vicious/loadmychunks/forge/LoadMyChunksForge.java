package com.vicious.loadmychunks.forge;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.client.LoadMyChunksClient;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LoadMyChunks.MOD_ID)
public class LoadMyChunksForge {
    public LoadMyChunksForge() {
        EventBuses.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus=Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
    public static class CMEs {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
            LoadMyChunksClient.init();
        }
    }



    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }
}