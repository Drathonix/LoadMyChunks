package com.drathonix.loadmychunks.common.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Multi-versioning Abstraction Utility class for the Brigadier API.
 * Subcommand structure is maintained by a list builder.
 */
public class Brigadier {
    /**
     * Creates a literal node.
     * @param name the literal name
     * @param inner child nodes.
     * @return a completed command.
     */
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> literal(@NotNull String name, @NotNull InternalBuilder<CommandSourceStack> inner){
        LiteralArgumentBuilder<CommandSourceStack> out = Commands.literal(name);
        for (ArgumentBuilder<CommandSourceStack, ?> builder : inner.make()) {
            out.then(builder);
        }
        return out;
    }

    /**
     * Creates an argument node.
     * @param name the arg name
     * @param type the arg type
     * @param inner child nodes.
     * @return a completed argument.
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull RequiredArgumentBuilder<CommandSourceStack,?> argument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull InternalBuilder<CommandSourceStack> inner){
        RequiredArgumentBuilder<CommandSourceStack,?> out = Commands.argument(name,type);
        for (ArgumentBuilder<CommandSourceStack, ?> builder : inner.make()) {
            out.then(builder);
        }
        return out;
    }

    /**
     * Sets the required permission level to 2.
     * @param argumentBuilder the node to apply to.
     * @return the modified node.
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull T admin(@NotNull ArgumentBuilder<CommandSourceStack,T> argumentBuilder){
        return argumentBuilder.requires(ctx->ctx.hasPermission(2));
    }

    /**
     * Adds a requirement condition for the command to be executable.
     * @param argumentBuilder the node to apply to.
     * @param predicate an arbitrary test condition.
     * @return the modified node.
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull ArgumentBuilder<CommandSourceStack,?> requires(@NotNull ArgumentBuilder<CommandSourceStack,T> argumentBuilder, @NotNull Predicate<CommandSourceStack> predicate){
        return argumentBuilder.requires(predicate);
    }

    /**
     * Creates a BlockPos argument node.
     * @param name the arg name.
     * @param inner child nodes.
     * @return the modified node
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull RequiredArgumentBuilder<CommandSourceStack,?> blockPos(@NotNull String name, @NotNull InternalBuilder<CommandSourceStack> inner){
        return argument(name, BlockPosArgument.blockPos(),inner);
    }

    /**
     * Creates a String argument node.
     * @param name the arg name.
     * @param inner child nodes.
     * @return the modified node
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull RequiredArgumentBuilder<CommandSourceStack,?> string(@NotNull String name, @NotNull InternalBuilder<CommandSourceStack> inner){
        return argument(name, StringArgumentType.string(),inner);
    }

    /**
     * Creates a Greedy String argument node.
     * @param name the arg name.
     * @param inner child nodes.
     * @return the modified node
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull RequiredArgumentBuilder<CommandSourceStack,?> stringRemaining(@NotNull String name, @NotNull InternalBuilder<CommandSourceStack> inner){
        return argument(name, StringArgumentType.greedyString(),inner);
    }

    /**
     * Creates a bool argument node.
     * @param name the arg name.
     * @param inner child nodes.
     * @return the modified node
     * @param <T> the argument builder type
     */
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> @NotNull RequiredArgumentBuilder<CommandSourceStack,?> bool(@NotNull String name, @NotNull InternalBuilder<CommandSourceStack> inner){
        return argument(name, BoolArgument.boolArgument(),inner);
    }

    /**
     *
     * @param argumentBuilder the source node.
     * @param executor an arbitrary executable.
     * @return the modified node.
     * @param <S> the command source type.
     * @param <T> the argument builder type.
     */
    public static <S,T extends ArgumentBuilder<S,T>> @NotNull T executes(@NotNull ArgumentBuilder<S,T> argumentBuilder, @NotNull Command<S> executor){
        return argumentBuilder.executes(executor);
    }

    /**
     * Gets the ServerLevel from a CommandContext of type CommandSourceStack.
     * @param ctx the context.
     * @return the ServerLevel of execution.
     */
    public static @NotNull ServerLevel getLevel(@NotNull CommandContext<CommandSourceStack> ctx) {
        return ctx.getSource().getLevel();
    }

    /**
     * Gets a block pos from an argument.
     * @param ctx the command context.
     * @param arg the argument name.
     * @return a blockPos
     * @throws CommandSyntaxException when the argument input is invalid.
     */
    public static @NotNull BlockPos getBlockPos(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull String arg) throws CommandSyntaxException {
        //? if <1.18.2 {
        return BlockPosArgument.getOrLoadBlockPos(ctx,arg);
        //?} else if <1.20.1 {
        /*return BlockPosArgument.getSpawnablePos(ctx,arg);
        *///?} else {
        /*return BlockPosArgument.getBlockPos(ctx, arg);
        *///?}
    }

    /**
     * Uses the context's blockpos if the provided pos is null.
     * @param ctx the command context.
     * @param bp a blockpos or null.
     * @return a blockPos
     */
    public static @NotNull BlockPos defaultedPos(@NotNull CommandContext<CommandSourceStack> ctx, @Nullable BlockPos bp) {
        if(bp != null){
            return bp;
        }
        else{
            Vec3 vec = ctx.getSource().getPosition();
            return new BlockPos((int)vec.x,(int)vec.y,(int)vec.z);
        }
    }

    /**
     * Creates a blockpos for the center of a chunk at a specific y.
     * @param pos the chunkpos to center on.
     * @param y the y level.
     * @return a BlockPos at the center of the chunk at the y.
     */
    public static @NotNull BlockPos centralized(@NotNull ChunkPos pos, int y) {
        return new BlockPos(pos.x*16+8,y,pos.z*16+8);
    }

    /**
     * Allows adding children to a command node using a pre-made list.
     * @param <S> the command source type.
     */
    @FunctionalInterface
    public interface InternalBuilder<S> extends Consumer<@NotNull List<ArgumentBuilder<S,?>>> {
        /**
         * Creates an argument builder list using the lambda expression.
         * @return an arbitrary list of ArgumentBuilders
         */
        default @NotNull List<ArgumentBuilder<S,?>> make(){
            List<ArgumentBuilder<S,?>> out = new ArrayList<>();
            accept(out);
            return out;
        }
    }
}
