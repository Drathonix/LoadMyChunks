package com.vicious.loadmychunks.client;

import com.vicious.loadmychunks.LoadMyChunks;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        ItemPropertiesRegistry.register(LoadMyChunks.itemTickometer.get(), new ResourceLocation("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LoadMyChunks.itemChunkometer.get(), new ResourceLocation("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,LoadMyChunks.LAG_READING_PACKET_ID,((buf, context) -> {
            lagLevel=buf.readFloat();
        }));
        //BlockEntityRenderers.register(LoadMyChunks.chunkLoaderBlockEntity.get(), LMCBasicBlockEntityRenderer::new);
    }
}
