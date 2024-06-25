//? if fabric {
package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.util.BoolArgument;
import com.vicious.loadmychunks.common.util.ModResource;
//? if >1.18.2
import com.vicious.loadmychunks.fabric.integ.CCTFabric;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.Registries;

public class LMCFabricInit {
    public static void init() {
        //? if >1.18.2 {
        ArgumentTypeRegistry.registerArgumentType(ModResource.of("lmcenum"), BoolArgument.class,new BoolArgument.Info());
        //?}
        ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
        //? if cct
        CCTFabric.init();
    }

    public static void clientInit(){
        //? if cct
        CCTFabric.clientInit();
    }
}
//?}
