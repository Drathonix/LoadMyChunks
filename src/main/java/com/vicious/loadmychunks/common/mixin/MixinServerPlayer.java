package com.vicious.loadmychunks.common.mixin;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.bridge.IInformable;
import com.vicious.loadmychunks.common.network.LagReadingPacket;
//? if >1.16.5
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
//? if <=1.16.5
/*import me.shedaniel.architectury.networking.NetworkManager;*/
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements IInformable {
    @Override
    public void informLagFrac(float frac) {
        //? if <=1.20.5 {
        FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
        newBuf.writeFloat(frac);
        NetworkManager.sendToPlayer(ServerPlayer.class.cast(this), LoadMyChunks.LAG_READING_PACKET_ID, newBuf);
        //?}
        //? if >1.20.5 {
        /*NetworkManager.sendToPlayer(ServerPlayer.class.cast(this),new LagReadingPacket(frac));
        *///?}
    }
}
