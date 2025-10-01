//? if fabric {
/*package com.drathonix.loadmychunks.fabric;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.integ.Integrations;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.registry.custom.LoaderTypeRegistry;
import com.drathonix.loadmychunks.common.util.BoolArgument;
import com.drathonix.loadmychunks.common.util.ModResource;
import net.fabricmc.api.ModInitializer;
//? if >1.18.2 {
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
//?}
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

public class LMCFabricInit implements ModInitializer {
    public static void init() {
        //? if >1.18.2 {
        ArgumentTypeRegistry.registerArgumentType(ModResource.of("lmcenum"), BoolArgument.class,new BoolArgument.Info());
        //?}
        ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
        //? if computercraft {
        /^Integrations.invokeWhenLoaded("computercraft","com.drathonix.loadmychunks.fabric.integ.CCTFabric","init",new Class[0]);
        ^///?}
        LoadMyChunks.modMode(()->{
            FabricRegistryBuilder.from(LoaderTypeRegistry.INSTANCE).buildAndRegister();
            FabricRegistryBuilder.from(LoadStateRegistry.INSTANCE).buildAndRegister();
        });
    }

    @Override
    public void onInitialize() {
        LoadMyChunks.init();
        LMCFabricInit.init();
    }
}
*///?}
