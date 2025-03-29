//? if neoforge {
/*package com.vicious.loadmychunks.neoforge;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.Integrations;
import com.vicious.loadmychunks.common.util.BoolArgument;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.InvocationTargetException;

import net.neoforged.fml.common.Mod;

@Mod(LoadMyChunks.MOD_ID)
public class LMCNeoInit {
    public LMCNeoInit(IEventBus meb) {
        LoadMyChunks.init();
        LMCNeoInit.init(meb);
    }
    public static void init(IEventBus meb) {
        NeoForge.EVENT_BUS.register(LMCNeoInit.class);
        meb.addListener(MMDNeo::newRegistry);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(meb);
        //? if cc-tweaked
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.neoforge.integ.CCTNeo","init",new Class[]{IEventBus.class},meb);
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event){
        LoadMyChunks.serverStarted(event.getServer());
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event){
        LoadMyChunks.serverStopped(event.getServer());
    }

    public static void newRegistry(NewRegistryEvent event){
        event.register(LoaderTypeRegistry.INSTANCE);
        event.register(LoadStateRegistry.INSTANCE);
    }
}
*///?}
