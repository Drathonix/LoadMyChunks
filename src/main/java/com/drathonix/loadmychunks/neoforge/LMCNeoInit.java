//? if neoforge {
package com.drathonix.loadmychunks.neoforge;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.integ.Integrations;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.registry.custom.LoaderTypeRegistry;
import com.drathonix.loadmychunks.common.util.BoolArgument;

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
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(LoadMyChunks.MOD_ID)
public class LMCNeoInit {
    public LMCNeoInit(IEventBus meb) {
        LoadMyChunks.init();
        LMCNeoInit.init(meb);
    }
    public static void init(IEventBus meb) {
        NeoForge.EVENT_BUS.register(LMCNeoInit.class);
        meb.addListener(LMCNeoInit::newRegistry);
        //TODO: WATCH NEO FOR CHANGES REGARDING THIS FEATURE.
        ArgumentTypeInfo<?,?> info = ArgumentTypeInfos.registerByClass(BoolArgument.class,new BoolArgument.Info());
        DeferredRegister<ArgumentTypeInfo<?,?>> args = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, LoadMyChunks.MOD_ID);
        args.register("lmcbool",()->info);
        args.register(meb);
        //? if computercraft
        /*Integrations.invokeWhenLoaded("computercraft","com.drathonix.loadmychunks.neoforge.integ.CCTNeo","init",new Class[]{IEventBus.class},meb);*/
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
        LoadMyChunks.modMode(()->{
            event.register(LoaderTypeRegistry.INSTANCE);
            event.register(LoadStateRegistry.INSTANCE);
        });
    }
}
//?}
