package com.vicious.loadmychunks.fabric;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import net.fabricmc.api.ClientModInitializer;

public class LoadMyChunksClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LoadMyChunksClient.init();
    }
}