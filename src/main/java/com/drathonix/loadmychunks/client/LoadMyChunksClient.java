package com.drathonix.loadmychunks.client;

import com.drathonix.loadmychunks.common.util.ModResource;
import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.registry.LMCContent;
import io.netty.buffer.Unpooled;
//TODO: unify netcode.
//? if >1.20.5 {
/*import com.drathonix.loadmychunks.common.network.LagReadingPacket;
import com.drathonix.loadmychunks.common.network.LagReadingRequest;
*///?}
//? if >1.16.5 {
import dev.architectury.networking.NetworkManager;
//?}
//? if >1.16.5 && <1.21.4 {
import dev.architectury.registry.item.ItemPropertiesRegistry;
//?}
//? if <=1.16.5 {
/*import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.ItemPropertiesRegistry;
*///?}
//? if <=1.20.5 {
import net.minecraft.network.FriendlyByteBuf;
//?}


public class LoadMyChunksClient {
    public static float lagLevel;

    public static void init(){
        LoadMyChunks.logger.info("Initializing Client Side");
        LoadMyChunks.modMode(()->{
            //? if >1.21.3 {

            //?} elif >1.20.5 {
            /*ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity, i) -> {
                NetworkManager.sendToServer(new LagReadingRequest());
                return LoadMyChunksClient.lagLevel;
            });
            ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity, i) -> {
                NetworkManager.sendToServer(new LagReadingRequest());
                return LoadMyChunksClient.lagLevel;
            });
            *///?}
            //? if >1.16.5 && <=1.20.5 {
            ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity, i) -> {
                NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
                return LoadMyChunksClient.lagLevel;
            });
            ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity, i) -> {
                NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
                return LoadMyChunksClient.lagLevel;
            });
            //?}
            //? if <=1.16.5 {
            /*ItemPropertiesRegistry.register(LMCContent.itemTickometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity) -> {
                NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
                return LoadMyChunksClient.lagLevel;
            });
            ItemPropertiesRegistry.register(LMCContent.itemChunkometer.get(), ModResource.of("lag"), (itemStack, clientLevel, livingEntity) -> {
                NetworkManager.sendToServer(LoadMyChunks.LAG_READING_PACKET_ID,new FriendlyByteBuf(Unpooled.buffer()));
                return LoadMyChunksClient.lagLevel;
            });
            *///?}
        });
        //? if <=1.20.5 {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,LoadMyChunks.LAG_READING_PACKET_ID,((buf, context) -> {
            lagLevel=buf.readFloat();
        }));
        //?}
        //? if >1.20.5 {
        /*NetworkManager.registerReceiver(NetworkManager.Side.S2C, LagReadingPacket.TYPE,LagReadingPacket.STREAM_CODEC,LagReadingPacket::handleClient);
        *///?}
    }

    /*protected void generateLagometer(Item item) {
        List<RangeSelectItemModel.Entry> list = new ArrayList<>();
        ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(ItemModelGenerators.createFlatItemModel(item, "_00", ModelTemplates.FLAT_ITEM));
        list.add(ItemModelUtils.override(itemmodel$unbaked, 0.0F));

        for (int i = 1; i < 64; i++) {
            ItemModel.Unbaked itemmodel$unbaked1 = ItemModelUtils.plainModel(
                    this.createFlatItemModel(item, String.format(Locale.ROOT, "_%02d", i), ModelTemplates.FLAT_ITEM)
            );
            list.add(ItemModelUtils.override(itemmodel$unbaked1, (float)i - 0.5F));
        }

        list.add(ItemModelUtils.override(itemmodel$unbaked, 63.5F));
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.inOverworld(
                                ItemModelUtils.rangeSelect(new Time(true, Time.TimeSource.DAYTIME), 64.0F, list),
                                ItemModelUtils.rangeSelect(new Time(true, Time.TimeSource.RANDOM), 64.0F, list)
                        )
                );
    }*/
}
