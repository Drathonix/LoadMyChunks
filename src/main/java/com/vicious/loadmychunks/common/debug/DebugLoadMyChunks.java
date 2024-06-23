package com.vicious.loadmychunks.common.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.blockentity.LMCBEType;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.registry.LMCRegistrar;
import com.vicious.loadmychunks.common.util.ModResource;
import me.shedaniel.architectury.event.events.CommandRegistrationEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DebugLoadMyChunks {
    static int laggerMsSleep = 1;

    static Supplier<BlockEntityType<DebugBlockEntityLagger>> laggerBlockEntity;

    public static void init(){
        LMCRegistrar.BLOCK.queue(reg->{
            Supplier<DebugBlockLagger> laggerBlock = LMCContent.registerBlockWithItem(reg,"lagger",()->new DebugBlockLagger(BlockBehaviour.Properties.of(Material.STONE)));
            LMCRegistrar.BLOCK_ENTITY_TYPE.queue(breg->{
                DebugLoadMyChunks.laggerBlockEntity = breg.register(ModResource.of("lagger"), () -> {
                    Set<Block> blocks = new HashSet<>();
                    blocks.add(laggerBlock.get());
                    return new LMCBEType<>(DebugBlockEntityLagger::new, blocks, null);
                });
            });
        });
        CommandRegistrationEvent.EVENT.register(DebugLoadMyChunks::registerCommands);
    }

    //? if <=1.16.5 {
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
    //?}
}
