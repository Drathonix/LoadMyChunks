package com.vicious.loadmychunks.forge;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.util.EnumArgument;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(LoadMyChunks.MOD_ID)
public class LoadMyChunksForge {
    public LoadMyChunksForge() {
        EventBuses.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        MinecraftForge.EVENT_BUS.register(this);
        DeferredRegister<ArgumentTypeInfo<?,?>> infos = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES,LoadMyChunks.MOD_ID);
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