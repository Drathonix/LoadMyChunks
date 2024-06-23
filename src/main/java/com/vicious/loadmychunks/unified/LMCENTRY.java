package com.vicious.loadmychunks.unified;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.util.BoolArgument;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.platform.forge.EventBuses;
*///?}

//? if >1.16.5
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

//? if fabric {
/*import com.vicious.loadmychunks.common.util.ModResource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
public class LMCENTRY implements ModInitializer {
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
/*import dev.architectury.platform.forge.EventBuses;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
//? if >1.16.5 {
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
//?}
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//? if <=1.16.5 {
/^import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
^///?}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
@Mod(LoadMyChunks.MOD_ID)
public class LMCENTRY {
    public LMCENTRY() {
        EventBuses.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        MinecraftForge.EVENT_BUS.register(this);
        //? if >1.16.5 {
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE,LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        //?}
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
*///?}

//? elif neoforge {
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;


@Mod(LoadMyChunks.MOD_ID)
public class LMCENTRY {
    public LMCENTRY(IEventBus modBus) {
        LoadMyChunks.init();
        NeoForge.EVENT_BUS.register(this);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE,LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(modBus);
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
//?}

