package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.common.LoadMyChunks;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.platform.forge.EventBuses;
*///?}
//? if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//?}
//? if fabric {
/*import com.vicious.loadmychunks.common.util.BoolArgument;
import com.vicious.loadmychunks.common.util.ModResource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
*///?}

//? if >1.16.5
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

//? if fabric {
/*public class LMCENTRY implements ModInitializer {
        @Override
        public void onInitialize() {
                LoadMyChunks.init();
                //? if >1.16.5 {
                ArgumentTypeRegistry.registerArgumentType(ModResource.of("lmcenum"), BoolArgument.class,new BoolArgument.Info());
                //?}
                ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
                ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
        }
}
*///?}

//? elif forge {
@Mod(LoadMyChunks.MOD_ID)
public class LMCENTRY {
    public LMCENTRY() {
        EventBuses.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        MinecraftForge.EVENT_BUS.register(this);
        //? if >1.16.5 {
        DeferredRegister<ArgumentTypeInfo<?,?>> infos = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES,LoadMyChunks.MOD_ID);
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(EnumArgument.class,new EnumArgument.Info());
        infos.register("lmcenum",()-> info);
        //?}
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
//?}

//? elif neoforge {
/*public class LMCENTRY {
}
*///?}

