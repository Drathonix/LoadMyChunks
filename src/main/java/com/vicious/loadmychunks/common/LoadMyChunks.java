package com.vicious.loadmychunks.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.persist.io.writer.wrapped.WrappedObject;
import com.vicious.persist.mappify.Mappifier;
import com.vicious.persist.mappify.registry.Stringify;
import com.vicious.persist.shortcuts.PersistShortcuts;

//? if >=1.20.6
import com.vicious.loadmychunks.common.integ.Integrations;
import com.vicious.loadmychunks.common.network.LagReadingPacket;
import com.vicious.loadmychunks.common.network.LagReadingRequest;
import com.vicious.loadmychunks.common.registry.LMCContent;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.TickDelayer;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.util.Brigadier;
import com.vicious.loadmychunks.common.util.Message;
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
//? if <1.20 {
/*import net.minecraft.world.phys.Vec3;
import java.util.function.Supplier;
*///?}

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
//? if <=1.20.4
/*import com.vicious.loadmychunks.common.util.ModResource;*/

public class LoadMyChunks {
	public static MinecraftServer server;
	public static final String MOD_ID = "loadmychunks";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static Level debugLevel = Level.DEBUG;

	//? if <1.20.5
	/*public static ResourceLocation LAG_READING_PACKET_ID = ModResource.of("lag");*/

	public static void init() {
		logger.info("Running with com.vicious.persist! " + PersistShortcuts.class);
		logger.info("Preparing to load your chunks...");
		LMCConfig.init();
		if(LMCConfig.useDebugLogging){
			logger.info("Changing to debug logging");
			debugLevel = Level.INFO;
			logger.info("Using Debug Logging");
		}
		CommandRegistrationEvent.EVENT.register(LoadMyChunks::registerCommands);
		LoadMyChunks.logger.info("Adding Chunk loader blocks");
		LMCContent.init();
		logger.info("Chunk Loader Loading Complete.");
		//? if <=1.20.5 {
		/*NetworkManager.registerReceiver(NetworkManager.Side.C2S, LAG_READING_PACKET_ID, ((buf, context) -> {
			Player plr = context.getPlayer();
			//? if =1.20.1 && forge {
			/^ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.getLevel(), plr.blockPosition());
			^///?} else if <1.19.5 {
			/^ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level, plr.blockPosition());
			^///?} else {
			ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level(), plr.blockPosition());
			//?}
			//TODO: integrate permissions with LP
			if (!LMCConfig.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID())) {
				cdm.addRecipient((IInformable) plr);
			}
		}));
		*///?}
		//? if >1.20.5 {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, LagReadingRequest.TYPE,LagReadingRequest.STREAM_CODEC, LagReadingRequest::handleServer);
		Integrations.invokeServer(()->{
			NetworkManager.registerS2CPayloadType(LagReadingPacket.TYPE,LagReadingPacket.STREAM_CODEC);
		});
		//?}
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

	//? <1.19 {
	/*public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) {
	*///?} else {
	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
	//?}
		dispatcher.register(Brigadier.admin(Brigadier.literal("loadmychunks",root->{
			root.add(Brigadier.executes(Brigadier.literal("forceload",forceLoad->{
				forceLoad.add(Brigadier.executes(Brigadier.bool("permanent",boolForceLoad->{
					boolForceLoad.add(Brigadier.executes(Brigadier.blockPos("pos",empty->{}),ctx->handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),Brigadier.getBlockPos(ctx,"pos"))));
				}),ctx->handleCMDForceload(ctx,ctx.getArgument("permanent",Boolean.class),null)));
			}),ctx->handleCMDForceload(ctx,true,null)));
			root.add(Brigadier.executes(Brigadier.literal("unforceload",unforceLoad->{
				unforceLoad.add(Brigadier.executes(Brigadier.bool("permanent",boolUnforceLoad->{
					boolUnforceLoad.add(Brigadier.executes(Brigadier.blockPos("pos",empty->{}),ctx->handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),Brigadier.getBlockPos(ctx,"pos"))));
				}),ctx->handleCMDUnforceload(ctx,ctx.getArgument("permanent",Boolean.class),null)));
			}),ctx->handleCMDUnforceload(ctx,false,null)));
			root.add(Brigadier.literal("config",config->{
				config.add(Brigadier.string("path",pathcmd->{
					pathcmd.add(Brigadier.executes(Brigadier.string("value",empty->{}),ctx->{
						String path = ctx.getArgument("path",String.class);
						String value = ctx.getArgument("value",String.class);
						Map<Object,Object> map = Mappifier.DEFAULT.mappify(LMCConfig.class).unwrap();
						String[] splitPath = path.split("/");
						Object o = map;
						for (int i = 0; i < splitPath.length-1; i++) {
							String s = splitPath[i];
							if(o instanceof WrappedObject){
								o = ((WrappedObject) o).object;
							}

							if(o instanceof Map){
								o = map.get(s);
								if(o == null){
									Message.sendSystem(ctx,Message.translatable("commands.loadmychunks.config.bad_path_not_found",s));
									return 0;
								}
							}
							else if(o instanceof List){
								List<?> list = (List<?>) o;
								try {
									o = list.get(Stringify.objectify(Integer.class, value));
								} catch (Throwable e) {
									Message.sendSystem(ctx,Message.translatable("commands.loadmychunks.config.invalid_integer" ,s));
									return 0;
								}
							}
						}
						String key = splitPath[splitPath.length-1];
						if(o instanceof Map){
							Map<Object,Object> m = (Map<Object,Object>) o;
							m.put(key,value);
						}
						if(o instanceof List){
							List<Object> l = (List<Object>) o;
							int k;
							try{
								k = Integer.parseInt(key);
							} catch (Throwable e) {
								Message.sendSystem(ctx,Message.translatable("commands.loadmychunks.config.invalid_integer",key));
								return 0;
							}
							l.set(k,value);
						}
						try{
							Mappifier.DEFAULT.unmappify(LMCConfig.class,map);
							PersistShortcuts.saveAsFile(LMCConfig.class);
						} catch (Throwable e) {
							Message.sendSystem(ctx,Message.translatable("commands.loadmychunks.config.invalid_value", value));
							return 0;
						}
						Message.sendSystem(ctx,Message.translatable("commands.loadmychunks.config.value_set",path, value));
						return 1;
					}));
				}));
			}));
			root.add(Brigadier.literal("list",list->{
				list.add(Brigadier.executes(Brigadier.literal("forced",empty->{}),ctx->{
					ServerLevel level = Brigadier.getLevel(ctx);
					Message.sendSystem(ctx,Message.styled(Message.translatable("commands.loadmychunks.list.forceloaded.header"),ChatFormatting.AQUA,true,true));
					ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState().shouldLoad()).forEach(cdm->{
						ChunkPos pos = cdm.getPosition();
						BlockPos dest = Brigadier.centralized(pos,255);
						if(cdm.getLoadState().permanent()) {
							Message.sendSystem(ctx,Message.clickCommand(Message.translatable("commands.loadmychunks.list.forceloaded.entry.permanent",pos.x,pos.z),"/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()));
						}
						else{
							Message.sendSystem(ctx,Message.clickCommand(Message.translatable("commands.loadmychunks.list.forceloaded.entry",pos.x,pos.z),"/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()));
						}
					});
					return 0;
				}));
				list.add(Brigadier.executes(Brigadier.literal("overticked",empty->{}),ctx->{
					ServerLevel level = Brigadier.getLevel(ctx);
					Message.sendSystem(ctx,Message.styled(Message.translatable("commands.loadmychunks.list.overticked.header"),ChatFormatting.AQUA,true,true));
					ChunkDataManager.getManager(level).getChunkDataModules().stream().filter(cdm-> cdm.getLoadState() == LoadStateEnum.OVERTICKED || cdm.getLoadState() == LoadStateEnum.PERMANENTLY_DISABLED).forEach(cdm->{
						ChunkPos pos = cdm.getPosition();
						BlockPos dest = Brigadier.centralized(pos,255);
						if(cdm.getLoadState() == LoadStateEnum.PERMANENTLY_DISABLED) {
							Message.sendSystem(ctx,Message.clickCommand(Message.translatable("commands.loadmychunks.list.forceloaded.entry.permanent",pos.x,pos.z),"/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()));
						}
						else{
							Message.sendSystem(ctx,Message.clickCommand(Message.translatable("commands.loadmychunks.list.forceloaded.entry",pos.x,pos.z),"/tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ()));
						}
					});
					return 0;
				}));
			}));
		})));
	}

	private static int handleCMDForceload(CommandContext<CommandSourceStack> ctx, boolean permanent, @Nullable BlockPos bp){
		bp = Brigadier.defaultedPos(ctx,bp);
		ChunkPos pos = new ChunkPos(bp);
		ServerLevel level = Brigadier.getLevel(ctx);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(level,pos);
		cdm.defaultLoadState=permanent ? LoadStateEnum.PERMANENT : LoadStateEnum.TICKING;
		cdm.clearCooldowns();
		cdm.update();
		cdm.getLoadState().apply(level,pos);
		if(permanent) {
			Message.sendSystem(ctx, Message.translatable("loadmychunks.command.forceload.set.permanent", pos.x, pos.z));
		} else {
			Message.sendSystem(ctx, Message.translatable("loadmychunks.command.forceload.set", pos.x, pos.z));
		}
		return 1;
	}

	private static int handleCMDUnforceload(CommandContext<CommandSourceStack> ctx, boolean ban, @Nullable BlockPos bp){
		bp = Brigadier.defaultedPos(ctx,bp);
		ChunkPos pos = new ChunkPos(bp);
		ServerLevel level = Brigadier.getLevel(ctx);
		ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(level,pos);
		cdm.defaultLoadState=ban ? LoadStateEnum.PERMANENTLY_DISABLED : LoadStateEnum.DISABLED;
		cdm.update();
		cdm.getLoadState().apply(level,pos);
		if(ban) {
			Message.sendSystem(ctx, Message.translatable("loadmychunks.command.forceload.unset.permanent", pos.x, pos.z));
		}
		else{
			Message.sendSystem(ctx, Message.translatable("loadmychunks.command.forceload.unset", pos.x, pos.z));
		}
		return 1;
	}
}
