package com.vicious.loadmychunks.common.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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

    public static void send(ServerPlayer player, Component message) {
        //? if >1.18.2
        player.sendSystemMessage(message);
        //? if <1.18.3
        /*player.sendMessage(message, Util.NIL_UUID);*/
    }

    public static void sendSuccess(CommandContext<CommandSourceStack> ctx, Component message) {
        //? if <1.20.1 {
        /*ctx.getSource().sendSuccess(message,true);
        *///?} else if >=1.20.1 && !forge {
        ctx.getSource().sendSuccess(()->message,true);
        //?} else if >=1.20.1 && forge {
        /*ctx.getSource().sendSystemMessage(message);
        *///?}
    }

    public static MutableComponent styled(MutableComponent component, ChatFormatting formatting, boolean bold, boolean underlined) {
        return component.setStyle(Style.EMPTY.withColor(formatting).withBold(bold).withUnderlined(underlined));
    }

    public static void sendSystem(CommandContext<CommandSourceStack> ctx, Component component) {
        //? if <1.19.2 {
        /*sendSuccess(ctx,component);
        *///?} else {
        ctx.getSource().sendSystemMessage(component);
        //?}
    }

    public static MutableComponent clickCommand(MutableComponent component, String command) {
        return component.setStyle(component.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,command)));
    }
}
