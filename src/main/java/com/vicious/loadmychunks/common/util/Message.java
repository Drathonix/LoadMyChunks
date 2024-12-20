package com.vicious.loadmychunks.common.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

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
        player.sendSystemMessage(message);
    }
}
