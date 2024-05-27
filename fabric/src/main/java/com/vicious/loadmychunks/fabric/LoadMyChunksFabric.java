package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.LoadMyChunks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class LoadMyChunksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LoadMyChunks.init();
        ServerLifecycleEvents.SERVER_STARTED.register(LoadMyChunks::serverStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(LoadMyChunks::serverStopped);
    }
}