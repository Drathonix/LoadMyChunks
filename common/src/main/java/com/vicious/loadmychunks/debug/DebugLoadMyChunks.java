package com.vicious.loadmychunks.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.block.LMCBEType;
import com.vicious.loadmychunks.util.ModResource;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashSet;
import java.util.Set;

public class DebugLoadMyChunks {
    static int laggerMsSleep = 1;

    static RegistrySupplier<BlockEntityType<DebugBlockEntityLagger>> laggerBlockEntity;

    public static void init(){
        RegistrySupplier<DebugBlockLagger> laggerBlock = LoadMyChunks.registerBlockWithItem("lagger",()->new DebugBlockLagger(BlockBehaviour.Properties.of(Material.STONE)));
        DebugLoadMyChunks.laggerBlockEntity = LoadMyChunks.BLOCKENTITIES.register(new ModResource("lagger"), () -> {
            Set<Block> blocks = new HashSet<>();
            blocks.add(laggerBlock.get());
            return new LMCBEType<>(DebugBlockEntityLagger::new, blocks, null);
        });
        CommandRegistrationEvent.EVENT.register(DebugLoadMyChunks::registerCommands);
    }

    static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("lmcdebug").then(Commands.literal("lagger").then(Commands.literal("sleep").executes(ctx->{
            ctx.getSource().sendSuccess(new TextComponent("Sleep time is " + laggerMsSleep),false);
            return 0;
        }).then(Commands.argument("delay",IntegerArgumentType.integer()).executes(ctx->{
            laggerMsSleep = IntegerArgumentType.getInteger(ctx,"delay");
            ctx.getSource().sendSuccess(new TextComponent("Sleep time set to " + laggerMsSleep),false);
            return 0;
        })))));
    }
}
