package com.vicious.loadmychunks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.debug.DebugLoadMyChunks;
import com.vicious.loadmychunks.network.LagReadingRequest;
import com.vicious.loadmychunks.registry.LMCContent;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import com.vicious.loadmychunks.system.TickDelayer;
import com.vicious.loadmychunks.system.control.LoadState;
import com.vicious.loadmychunks.util.BoolArgument;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadMyChunks {
	public static MinecraftServer server;
	public static final String MOD_ID = "loadmychunks";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static Level debugLevel = Level.DEBUG;

	public static void init() {
		logger.info("Preparing to load your chunks...");
		LMCConfig.init();
		if(LMCConfig.instance.useDebugLogging){
			logger.info("Changing to debug logging");
			debugLevel = Level.INFO;
			logger.info("Using Debug Logging");
		}

		CommandRegistrationEvent.EVENT.register(LoadMyChunks::registerCommands);
		LoadMyChunks.logger.info("Adding Chunk loader blocks");
		LMCContent.init();
		if(allowUsingDebugFeatures()){
			DebugLoadMyChunks.init();
		}
		logger.info("Chunk Loader Loading Complete.");
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, LagReadingRequest.TYPE,LagReadingRequest.STREAM_CODEC, LagReadingRequest::handleServer);
	}



	public static void serverStarted(MinecraftServer server) {
		LoadMyChunks.server = server;
		server.addTickable(TickDelayer::tick);
	}

	public static void serverStopped(MinecraftServer server) {
		ChunkDataManager.clear();
	}

	public static boolean allowUsingDebugFeatures() {
		return false;
	}

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("loadmychunks").requires(ctx-> ctx.hasPermission(2));
		root.then(Commands.literal("forceload").executes(ctx-> handleCMDForceload(ctx,true,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),BlockPosArgument.getBlockPos(ctx,"pos"))))));
		root.then(Commands.literal("unforceload").executes(ctx-> handleCMDUnforceload(ctx,false,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),BlockPosArgument.getBlockPos(ctx,"pos"))))));
		root.then(Commands.literal("list").then(Commands.literal("forced").executes(ctx->{
			ServerLevel level = ctx.getSource().getLevel();
			ctx.getSource().sendSystemMessage(Component.literal("Forceloaded Chunks").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(true).withUnderlined(true)));
			ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState().shouldLoad()).forEach(cdm->{
				ChunkPos pos = cdm.getPosition();
				BlockPos dest = new BlockPos(pos.getMiddleBlockX(), 255, pos.getMiddleBlockZ());
				if(cdm.getLoadState().permanent()) {
					ctx.getSource().sendSystemMessage(Component.literal("(" + pos.x + "," + pos.z + ") permanent").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))));
				}
				else{
					ctx.getSource().sendSystemMessage(Component.literal("(" + pos.x + "," + pos.z + ")").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))));
				}
			});
			return 0;
		})).then(Commands.literal("overticked").executes(ctx->{
			ServerLevel level = ctx.getSource().getLevel();
			ctx.getSource().sendSystemMessage(Component.literal("Overticked Chunks").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(true).withUnderlined(true)));
			ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState() == LoadState.OVERTICKED).forEach(cdm->{
				ChunkPos pos = cdm.getPosition();
				BlockPos dest = new BlockPos(pos.getMiddleBlockX(), 255, pos.getMiddleBlockZ());
				if(cdm.getLoadState() == LoadState.PERMANENTLY_DISABLED) {
					ctx.getSource().sendSystemMessage(Component.literal("(" + pos.x + "," + pos.z + ") permanently disabled").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))));
				}
				else{
					ctx.getSource().sendSystemMessage(Component.literal("(" + pos.x + "," + pos.z + ")").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))));
				}
			});
			return 0;
		})));
		dispatcher.register(root);
	}

	private static int handleCMDForceload(CommandContext<CommandSourceStack> ctx, boolean permanent, BlockPos bp){
		bp = bp == null ? BlockPos.containing(ctx.getSource().getPosition()) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=permanent ? LoadState.PERMANENT : LoadState.TICKING;
		cdm.clearCooldowns();
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(()->{
			if(permanent) {
				return Component.translatable("loadmychunks.command.forceload.set.permanent",pos.x,pos.z);
			}
			else{
				return Component.translatable("loadmychunks.command.forceload.set",pos.x,pos.z);
			}
		},true);
		return 0;
	}

	private static int handleCMDUnforceload(CommandContext<CommandSourceStack> ctx, boolean ban, BlockPos bp){
		bp = bp == null ? BlockPos.containing(ctx.getSource().getPosition()) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=ban ? LoadState.PERMANENTLY_DISABLED : LoadState.DISABLED;
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(()->{
			if(ban) {
				return Component.translatable("loadmychunks.command.forceload.unset.permanent",pos.x,pos.z);
			}
			else{
				return Component.translatable("loadmychunks.command.forceload.unset",pos.x,pos.z);
			}
		},true);
		return 0;
	}
}
