package com.vicious.loadmychunks.common.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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

import java.util.function.Predicate;

public class Brigadier {
    public static LiteralArgumentBuilder<CommandSourceStack> literal(String name){
        return Commands.literal(name);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> argument(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, String name, ArgumentType<?> type){
        return argumentBuilder.then(Commands.argument(name,type));
    }
    public static LiteralArgumentBuilder<CommandSourceStack> admin(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder){
        return argumentBuilder.requires(ctx->ctx.hasPermission(2));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> literal(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, String name){
        return argumentBuilder.then(literal(name));
    }

    public static <S> LiteralArgumentBuilder<S> requires(LiteralArgumentBuilder<S> argumentBuilder, Predicate<S> predicate){
        return argumentBuilder.requires(predicate);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> blockPos(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, String name){
        return argument(argumentBuilder,name, BlockPosArgument.blockPos());
    }

    public static <S> LiteralArgumentBuilder<S> executes(LiteralArgumentBuilder<S> argumentBuilder, Command<S> executor){
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
        return new BlockPos(pos.x+8,y,pos.z+8);
    }
}
