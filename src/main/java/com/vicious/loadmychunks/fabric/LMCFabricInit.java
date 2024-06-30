//? if fabric {
/*package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.Integrations;
import com.vicious.loadmychunks.common.util.BoolArgument;
import com.vicious.loadmychunks.common.util.ModResource;
//? if >1.18.2
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class LMCFabricInit {
    public static void init() {
        //? if >1.18.2 {
        ArgumentTypeRegistry.registerArgumentType(ModResource.of("lmcenum"), BoolArgument.class,new BoolArgument.Info());
        //?}
        ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
        //? if cct
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.fabric.integ.CCTFabric","init",new Class[0]);
    }

    public static void clientInit(){
        //? if cct
        Integrations.invokeWhenLoaded("computercraft","com.vicious.loadmychunks.fabric.integ.CCTFabric","clientInit",new Class[0]);
    }
}
*///?}
