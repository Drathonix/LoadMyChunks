package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.bridge.IMixinArgumentTypeInfos;
import com.vicious.loadmychunks.util.EnumArgument;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

public class LoadMyChunksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LoadMyChunks.init();
        ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);

        IMixinArgumentTypeInfos infos = IMixinArgumentTypeInfos.class.cast(new ArgumentTypeInfos());
        infos.lmc$register("lmcenum", EnumArgument.class,new EnumArgument.Info<>());

    }
}