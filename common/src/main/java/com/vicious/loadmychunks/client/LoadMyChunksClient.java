package com.vicious.loadmychunks.client;

import com.vicious.loadmychunks.registry.LMCContent;
import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.network.LagReadingPacket;
import com.vicious.loadmychunks.network.LagReadingRequest;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.resources.ResourceLocation;

public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ResourceLocation.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ResourceLocation.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LagReadingPacket.TYPE,LagReadingPacket.STREAM_CODEC,LagReadingPacket::handleClient);
        //BlockEntityRenderers.register(LoadMyChunks.chunkLoaderBlockEntity.get(), LMCBasicBlockEntityRenderer::new);
    }
}
