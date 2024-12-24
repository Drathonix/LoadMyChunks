package com.vicious.loadmychunks.common.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

//? if <1.18.3 {
/*import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
*///?}

public class Message {
    public static MutableComponent translatable(String key, Object... args) {
        //? if <1.18.3 {
        /*return new TranslatableComponent(key,args);
        *///?}
        //? if >1.18.2 {
        return Component.translatable(key,args);
        //?}
    }

    public static void send(ServerPlayer player, MutableComponent message) {
        //? if >1.18.2
        player.sendSystemMessage(message);
        //? if <1.18.3
        /*player.sendMessage(message, Util.NIL_UUID);*/
    }
}
