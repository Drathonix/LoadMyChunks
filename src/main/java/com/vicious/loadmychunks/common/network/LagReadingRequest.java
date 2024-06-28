package com.vicious.loadmychunks.common.network;

//TODO: unify across all vers.
//? if <=1.20.5 {
/*public class LagReadingRequest{}
*///?}

//? if >1.20.5 {
import com.vicious.loadmychunks.common.bridge.IInformable;
import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.util.ModResource;
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
            cdm.addRecipient((IInformable) plr);
        }
    }
}
//?}