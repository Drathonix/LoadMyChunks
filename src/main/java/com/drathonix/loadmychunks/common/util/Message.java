package com.drathonix.loadmychunks.common.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if <1.18.3 {
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.Util;
//?}

/**
 * Multi-versioning abstraction utility class for Components/Chat
 */
public class Message {
    /**
     * Creates a translatable Component
     * @param key the translation key
     * @param args the translation arguments
     * @return a MutableComponent containing translatable contents.
     */
    public static @NotNull MutableComponent translatable(@NotNull String key, Object... args) {
        //? if <1.18.3 {
        return new TranslatableComponent(key,args);
        //?}
        //? if >1.18.2 {
        /*return Component.translatable(key,args);
        *///?}
    }

    /**
     * Sends a system message to a player.
     * @param player the recipient.
     * @param message the message.
     */
    public static void send(@NotNull ServerPlayer player, @Nullable Component message) {
        if(message == null){
            return;
        }
        //? if >1.18.2 {
        /*player.sendSystemMessage(message);
        *///?} else {
        player.sendMessage(message, Util.NIL_UUID);
        //?}
    }

    /**
     * Sends a success message to a command source.
     * @param ctx the command context.
     * @param message the success message
     */
    public static void sendSuccess(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Component message) {
        //? if <1.20.1 {
        ctx.getSource().sendSuccess(message,true);
        //?} else if >=1.20.1 && !forge {
        /*ctx.getSource().sendSuccess(()->message,true);
        *///?} else if >=1.20.1 && forge {
        /*ctx.getSource().sendSystemMessage(message);
        *///?}
    }

    /**
     * Styles a component.
     * @param component the component to style.
     * @param formatting the color
     * @param bold whether to bold the text.
     * @param underlined whether to underline the text.
     * @return the styled component.
     */
    public static @NotNull MutableComponent styled(@NotNull MutableComponent component, @NotNull ChatFormatting formatting, boolean bold, boolean underlined) {
        return component.setStyle(Style.EMPTY.withColor(formatting).withBold(bold).withUnderlined(underlined));
    }

    /**
     * Sends a system message to a command source.
     * @param ctx the command context.
     * @param component the message.
     */
    public static void sendSystem(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Component component) {
        //? if <1.19.2 {
        sendSuccess(ctx,component);
        //?} else {
        /*ctx.getSource().sendSystemMessage(component);
        *///?}
    }

    /**
     * Sets the component on click to suggest a command.
     * @param component the component.
     * @param command the suggested command.
     * @return the component with the click command.
     */
    public static @NotNull MutableComponent clickCommand(@NotNull MutableComponent component, @NotNull String command) {
        return component.setStyle(component.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,command)));
    }

    /**
     * Creates an empty component.
     * @return a new empty component.
     */
    public static MutableComponent empty() {
        return literal("");
    }

    /**
     * Creates a literal text component.
     * @param text the text.
     * @return a Text Component
     */
    public static MutableComponent literal(String text) {
        //? if <1.18.3 {
        return new TextComponent(text);
         //?}
        //? if >1.18.2 {
        /*return Component.literal(text);
        *///?}
    }

    public static MutableComponent append(@NotNull MutableComponent m1, @NotNull Component m2) {
        return m1.append(m2);
    }

    public static MutableComponent append(MutableComponent m1, String str) {
        return m1.append(str);
    }
}
