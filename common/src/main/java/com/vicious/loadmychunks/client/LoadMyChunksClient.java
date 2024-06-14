package com.vicious.loadmychunks.client;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.network.LagReadingRequest;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.resources.ResourceLocation;

public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        ItemPropertiesRegistry.register(LoadMyChunks.itemTickometer.get(), ResourceLocation.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LoadMyChunks.itemChunkometer.get(), ResourceLocation.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });

        //BlockEntityRenderers.register(LoadMyChunks.chunkLoaderBlockEntity.get(), LMCBasicBlockEntityRenderer::new);
    }
}
