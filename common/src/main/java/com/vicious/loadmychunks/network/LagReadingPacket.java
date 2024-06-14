package com.vicious.loadmychunks.network;

import com.vicious.loadmychunks.client.LoadMyChunksClient;
import com.vicious.loadmychunks.util.ModResource;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record LagReadingPacket(float lag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LagReadingPacket> TYPE = new CustomPacketPayload.Type<>(ModResource.of("lag"));
    public static final StreamCodec<ByteBuf, LagReadingPacket> STREAM_CODEC = StreamCodec.of(
            (object, object2) -> object.writeFloat(object2.lag), object -> new LagReadingPacket(object.readFloat()));

    @NotNull
    @Override
    public CustomPacketPayload.Type<LagReadingPacket> type() {
        return TYPE;
    }

    public void handleClient(NetworkManager.PacketContext packetContext) {
        LoadMyChunksClient.lagLevel=lag;
    }
}
