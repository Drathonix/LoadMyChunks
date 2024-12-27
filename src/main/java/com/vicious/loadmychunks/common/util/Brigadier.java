package com.vicious.loadmychunks.common.util;

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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Brigadier {
    public static LiteralArgumentBuilder<CommandSourceStack> literal(String name, InternalBuilder<CommandSourceStack> inner){
        LiteralArgumentBuilder<CommandSourceStack> out = Commands.literal(name);
        for (ArgumentBuilder<CommandSourceStack, ?> builder : inner.make()) {
            out.then(builder);
        }
        return out;
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> RequiredArgumentBuilder<CommandSourceStack,?> argument(String name, ArgumentType<?> type, InternalBuilder<CommandSourceStack> inner){
        RequiredArgumentBuilder<CommandSourceStack,?> out = Commands.argument(name,type);
        for (ArgumentBuilder<CommandSourceStack, ?> builder : inner.make()) {
            out.then(builder);
        }
        return out;
    }
    public static <T extends ArgumentBuilder<CommandSourceStack,T>> T admin(ArgumentBuilder<CommandSourceStack,T> argumentBuilder){
        return argumentBuilder.requires(ctx->ctx.hasPermission(2));
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> ArgumentBuilder<CommandSourceStack,?> requires(ArgumentBuilder<CommandSourceStack,T> argumentBuilder, Predicate<CommandSourceStack> predicate){
        return argumentBuilder.requires(predicate);
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> RequiredArgumentBuilder<CommandSourceStack,?> blockPos(String name, InternalBuilder<CommandSourceStack> inner){
        return argument(name, BlockPosArgument.blockPos(),inner);
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> RequiredArgumentBuilder<CommandSourceStack,?> string(String name, InternalBuilder<CommandSourceStack> inner){
        return argument(name, StringArgumentType.string(),inner);
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> RequiredArgumentBuilder<CommandSourceStack,?> stringRemaining(String name, InternalBuilder<CommandSourceStack> inner){
        return argument(name, StringArgumentType.greedyString(),inner);
    }

    public static <T extends ArgumentBuilder<CommandSourceStack,T>> RequiredArgumentBuilder<CommandSourceStack,?> bool(String name, InternalBuilder<CommandSourceStack> inner){
        return argument(name, BoolArgument.boolArgument(),inner);
    }

    public static <S,T extends ArgumentBuilder<S,T>> T executes(ArgumentBuilder<S,T> argumentBuilder, Command<S> executor){
        return argumentBuilder.executes(executor);
    }

    public static ServerLevel getLevel(CommandContext<CommandSourceStack> ctx) {
        return ctx.getSource().getLevel();
    }

    public static BlockPos getBlockPos(CommandContext<CommandSourceStack> ctx, String arg) throws CommandSyntaxException {
        //? if <1.18.2 {
        /*return BlockPosArgument.getOrLoadBlockPos(ctx,arg);*/
        //?} else if <1.20.1 {
        /*return BlockPosArgument.getSpawnablePos(ctx,arg);
        *///?} else {
        return BlockPosArgument.getBlockPos(ctx, arg);
        //?}
    }

    public static BlockPos defaultedPos(CommandContext<CommandSourceStack> ctx, @Nullable BlockPos bp) {
        if(bp != null){
            return bp;
        }
        else{
            Vec3 vec = ctx.getSource().getPosition();
            return new BlockPos((int)vec.x,(int)vec.y,(int)vec.z);
        }
    }

    public static BlockPos centralized(ChunkPos pos, int y) {
        return new BlockPos(pos.x*16+8,y,pos.z*16+8);
    }

    @FunctionalInterface
    public interface InternalBuilder<S> extends Consumer<List<ArgumentBuilder<S,?>>> {
        default List<ArgumentBuilder<S,?>> make(){
            List<ArgumentBuilder<S,?>> out = new ArrayList<>();
            accept(out);
            return out;
        }
    }
}
