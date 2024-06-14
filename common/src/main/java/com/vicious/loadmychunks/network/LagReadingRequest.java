package com.vicious.loadmychunks.network;

import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import com.vicious.loadmychunks.util.ModResource;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record LagReadingRequest() implements CustomPacketPayload {

    public static final Type<LagReadingRequest> TYPE = new Type<>(ModResource.of("lag_request"));
    public static final StreamCodec<ByteBuf, LagReadingRequest> STREAM_CODEC = StreamCodec.of(
            (object, object2) -> {}, object -> new LagReadingRequest());

    @NotNull
    @Override
    public CustomPacketPayload.Type<LagReadingRequest> type() {
        return TYPE;
    }


    public void handleServer(NetworkManager.PacketContext context) {
        Player plr = context.getPlayer();
        ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level(), plr.blockPosition());
        //TODO: integrate permissions with LP
        if (!LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID())) {
            cdm.timeRegardless = true;
            cdm.addRecipient((ServerPlayer) plr);
        }
    }
}
