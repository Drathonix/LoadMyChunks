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

public class LMCNeoInit {
    public static void init(IEventBus meb) {
        NeoForge.EVENT_BUS.register(LMCNeoInit.class);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(meb);
        //? if cct
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

    public static void clientInit() {
        //? if cct
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.neoforge.integ.CCTNeo","clientInit",new Class[0]);
    }
}
*///?}
