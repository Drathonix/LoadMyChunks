package com.drathonix.loadmychunks.client;

import com.drathonix.loadmychunks.common.util.ModResource;
import com.drathonix.loadmychunks.common.LoadMyChunks;
//TODO: unify netcode.
//? if >1.20.5 {
import com.drathonix.loadmychunks.common.network.LagReadingPacket;
import com.drathonix.loadmychunks.common.network.LagReadingRequest;
//?}
import com.drathonix.loadmychunks.common.registry.LMCContent;
//? if >1.16.5 {
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.item.ItemPropertiesRegistry;
//?}
//? if <=1.16.5 {
/*import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.ItemPropertiesRegistry;
*///?}
//? if <=1.20.5
/*import net.minecraft.network.FriendlyByteBuf;*/

public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        //? if >1.20.5 {
        ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(new LagReadingRequest());
            return LoadMyChunksClient.lagLevel;
        });
        //?}
        //? if >1.16.5 && <=1.20.5 {
        /*ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity, i) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        *///?}
        //? if <=1.16.5 {
        /*ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.parse("lag"), (itemStack, clientLevel, livingEntity) -> {
            NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
            return LoadMyChunksClient.lagLevel;
        });
        *///?}
        //? if <=1.20.5 {
        /*NetworkManager.registerReceiver(NetworkManager.Side.S2C,LoadMyChunks.LAG_READING_PACKET_ID,((buf, context) -> {
            lagLevel=buf.readFloat();
        }));
        *///?}
        //? if >1.20.5 {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LagReadingPacket.TYPE,LagReadingPacket.STREAM_CODEC,LagReadingPacket::handleClient);
        //?}
    }
}
