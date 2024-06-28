//? if forge {
/*package com.vicious.loadmychunks.forge;

import com.vicious.loadmychunks.common.util.BoolArgument;
//? if <=1.16.5
import me.shedaniel.architectury.platform.forge.EventBuses;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.Integrations;
//? if >1.18.2 {
/^import com.vicious.loadmychunks.common.util.BoolArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
^///?}

//? if >1.19.3
/^import net.minecraft.core.registries.Registries;^/

//? if >1.16.5 {
/^import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.Integrations;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import dev.architectury.platform.forge.EventBuses;
^///?}

//? if <=1.16.5 {
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
//?}

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;


public class LMCForge {
    public static void init() {
        EventBuses.registerModEventBus(LoadMyChunks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LoadMyChunks.init();
        MinecraftForge.EVENT_BUS.register(LMCForge.class);
        //? if >1.18.2 && <=1.19.3 {
        /^ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY,LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        ^///?}
        //? if >1.19.3 {
        /^ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE,LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        ^///?}
        //? if cct
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.forge.integ.CCTForge","init",new Class[]{IEventBus.class}, FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void clientInit() {
        //? if cct
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.forge.integ.CCTForge","clientInit",new Class[0]);
    }


    //? if <=1.16.5 {
    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }
    //?}

    //? if >1.16.5 {
    /^@SubscribeEvent
    public static void serverStarted(ServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }
    ^///?}
}
*///?}
