package com.vicious.loadmychunks.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vicious.loadmychunks.common.bridge.IInformable;
import com.vicious.loadmychunks.common.config.LMCConfig;
//? if >=1.20.6
/*import com.vicious.loadmychunks.common.network.LagReadingRequest;*/
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.TickDelayer;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.util.BoolArgument;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.event.events.CommandRegistrationEvent;
import me.shedaniel.architectury.networking.NetworkManager;
*///?}
//? if >1.16.5 {
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.networking.NetworkManager;
//?}
import net.minecraft.ChatFormatting;
//? if >1.18.2
import net.minecraft.commands.CommandBuildContext;
//? if <1.18.3
/*import net.minecraft.network.chat.TextComponent;*/

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//? if <=1.20.4
import com.vicious.loadmychunks.common.util.ModResource;


public class LoadMyChunks {
	public static MinecraftServer server;
	public static final String MOD_ID = "loadmychunks";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static Level debugLevel = Level.DEBUG;

	//? if <1.20.5
	public static ResourceLocation LAG_READING_PACKET_ID = ModResource.of("lag");

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
		logger.info("Chunk Loader Loading Complete.");
		//? if <=1.20.5 {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, LAG_READING_PACKET_ID, ((buf, context) -> {
			Player plr = context.getPlayer();
			//? if <1.19.5
			/*ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level, plr.blockPosition());*/
			//? if >1.19.4
			ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level(), plr.blockPosition());
			//TODO: integrate permissions with LP
			if (!LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID())) {
				cdm.addRecipient((IInformable) plr);
			}
		}));
		//?}
		//? if >1.20.5 {
		/*NetworkManager.registerReceiver(NetworkManager.Side.C2S, LagReadingRequest.TYPE,LagReadingRequest.STREAM_CODEC, LagReadingRequest::handleServer);
		*///?}
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

	//? if >1.18.2 && <1.19.5 {
	/*public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("loadmychunks").requires(ctx-> ctx.hasPermission(2));
		root.then(Commands.literal("forceload").executes(ctx-> handleCMDForceload(ctx,true,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),BlockPosArgument.getSpawnablePos(ctx,"pos"))))));
		root.then(Commands.literal("unforceload").executes(ctx-> handleCMDUnforceload(ctx,false,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),BlockPosArgument.getSpawnablePos(ctx,"pos"))))));
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
		Vec3 v = ctx.getSource().getPosition();
		//? if <=1.19.3 {
		/^bp = bp == null ? new BlockPos(v.x,v.y,v.z) : bp;
		^///?}
		//? if >1.19.3 && <1.19.5
		bp = bp == null ? BlockPos.containing(v) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=permanent ? LoadState.PERMANENT : LoadState.TICKING;
		cdm.clearCooldowns();
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(((Supplier<Component>)()->{
			if(permanent) {
				return Component.translatable("loadmychunks.command.forceload.set.permanent",pos.x,pos.z);
			}
			else{
				return Component.translatable("loadmychunks.command.forceload.set",pos.x,pos.z);
			}
		}).get(),true);
		return 0;
	}

	private static int handleCMDUnforceload(CommandContext<CommandSourceStack> ctx, boolean ban, BlockPos bp){
		Vec3 v = ctx.getSource().getPosition();
		//? if <=1.19.3 {
		/^bp = bp == null ? new BlockPos(v.x,v.y,v.z) : bp;
		^///?}
		//? if >1.19.3 && <1.19.5
		bp = bp == null ? BlockPos.containing(v) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=ban ? LoadState.PERMANENTLY_DISABLED : LoadState.DISABLED;
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(((Supplier<Component>)()->{
			if(ban) {
				return Component.translatable("loadmychunks.command.forceload.unset.permanent",pos.x,pos.z);
			}
			else{
				return Component.translatable("loadmychunks.command.forceload.unset",pos.x,pos.z);
			}
		}).get(),true);
		return 0;
	}
	*///?}

	//? if >1.19.4 {
	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("loadmychunks").requires(ctx-> ctx.hasPermission(2));
		root.then(Commands.literal("forceload").executes(ctx-> handleCMDForceload(ctx,true,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent", Boolean.class),BlockPosArgument.getBlockPos(ctx,"pos"))))));
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
			ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState() == LoadState.OVERTICKED || cdm.getLoadState() == LoadState.PERMANENTLY_DISABLED).forEach(cdm->{
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
	//?}

	//? if <1.18.3 {
	/*public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("loadmychunks").requires(ctx-> ctx.hasPermission(2));
		root.then(Commands.literal("forceload").executes(ctx-> handleCMDForceload(ctx,true,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),getBlockPos(ctx,"pos"))))));
		root.then(Commands.literal("unforceload").executes(ctx-> handleCMDUnforceload(ctx,false,null)).then(Commands.argument("permanent", BoolArgument.boolArgument()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),getBlockPos(ctx,"pos"))))));
		root.then(Commands.literal("list").then(Commands.literal("forced").executes(ctx->{
			ServerLevel level = ctx.getSource().getLevel();
			ctx.getSource().sendSuccess(new TextComponent("Forceloaded Chunks").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(true).withUnderlined(true)),false);
			ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState().shouldLoad()).forEach(cdm->{
				ChunkPos pos = cdm.getPosition();
				BlockPos dest = new BlockPos(pos.getMinBlockX(), 255, pos.getMaxBlockX());
				if(cdm.getLoadState().permanent()) {
					ctx.getSource().sendSuccess(new TextComponent("(" + pos.x + "," + pos.z + ") permanent").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))),false);
				}
				else{
					ctx.getSource().sendSuccess(new TextComponent("(" + pos.x + "," + pos.z + ")").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))),false);
				}
			});
			return 0;
		})).then(Commands.literal("overticked").executes(ctx->{
			ServerLevel level = ctx.getSource().getLevel();
			ctx.getSource().sendSuccess(new TextComponent("Overticked Chunks").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(true).withUnderlined(true)),false);
			ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState() == LoadState.OVERTICKED).forEach(cdm->{
				ChunkPos pos = cdm.getPosition();
				BlockPos dest = new BlockPos(pos.getMinBlockX(), 255, pos.getMaxBlockX());
				if(cdm.getLoadState() == LoadState.PERMANENTLY_DISABLED) {
					ctx.getSource().sendSuccess(new TextComponent("(" + pos.x + "," + pos.z + ") permanently disabled").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))),false);
				}
				else{
					ctx.getSource().sendSuccess(new TextComponent("(" + pos.x + "," + pos.z + ")").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()))),false);
				}
			});
			return 0;
		})));
		dispatcher.register(root);
	}

	private static int handleCMDForceload(CommandContext<CommandSourceStack> ctx, boolean permanent, BlockPos bp){
		Vec3 v = ctx.getSource().getPosition();
		bp = bp == null ? new BlockPos(v.x,v.y,v.z) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=permanent ? LoadState.PERMANENT : LoadState.TICKING;
		cdm.clearCooldowns();
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(((Supplier<Component>)()->{
			if(permanent) {
				return new TranslatableComponent("loadmychunks.command.forceload.set.permanent",pos.x,pos.z);
			}
			else{
				return new TranslatableComponent("loadmychunks.command.forceload.set",pos.x,pos.z);
			}
		}).get(),true);
		return 0;
	}

	private static int handleCMDUnforceload(CommandContext<CommandSourceStack> ctx, boolean ban, BlockPos bp){
		Vec3 v = ctx.getSource().getPosition();
		bp = bp == null ? new BlockPos(v.x,v.y,v.z) : bp;
		ChunkPos pos = new ChunkPos(bp);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(ctx.getSource().getLevel(),pos);
		cdm.defaultLoadState=ban ? LoadState.PERMANENTLY_DISABLED : LoadState.DISABLED;
		cdm.update();
		cdm.getLoadState().apply(ctx.getSource().getLevel(),pos);
		ctx.getSource().sendSuccess(((Supplier<Component>)()->{
			if(ban) {
				return new TranslatableComponent("loadmychunks.command.forceload.unset.permanent",pos.x,pos.z);
			}
			else{
				return new TranslatableComponent("loadmychunks.command.forceload.unset",pos.x,pos.z);
			}
		}).get(),true);
		return 0;
	}
	*///?}

	public static BlockPos getBlockPos(CommandContext<CommandSourceStack> ctx, String key){
		//? if >1.18.1 {
        try {
            return BlockPosArgument.getLoadedBlockPos(ctx,key);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
		//?}
		//? if <=1.18.1 {
		/*try {
			return BlockPosArgument.getOrLoadBlockPos(ctx,key);
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		*///?}
    }
}
