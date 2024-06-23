package com.vicious.loadmychunks.client;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.registry.LMCContent;
//? if >1.16.5 {
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.item.ItemPropertiesRegistry;
//?}
import io.netty.buffer.Unpooled;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.ItemPropertiesRegistry;
*///?}
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        //? if >1.16.5 {
        ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), new ResourceLocation("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), new ResourceLocation("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        //?}
        //? if <=1.16.5 {
        /*ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), new ResourceLocation("lag"), (ItemPropertyFunction)(itemStack, clientLevel, livingEntity) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), new ResourceLocation("lag"), (ItemPropertyFunction)(itemStack, clientLevel, livingEntity) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        *///?}
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,LoadMyChunks.LAG_READING_PACKET_ID,((buf, context) -> {
            lagLevel=buf.readFloat();
        }));
    }
}
