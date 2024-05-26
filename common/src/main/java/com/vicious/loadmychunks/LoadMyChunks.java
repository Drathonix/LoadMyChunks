package com.vicious.loadmychunks;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.vicious.loadmychunks.block.BlockChunkLoader;
import com.vicious.loadmychunks.block.BlockEntityChunkLoader;
import com.vicious.loadmychunks.block.LMCBEType;
import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.debug.DebugLoadMyChunks;
import com.vicious.loadmychunks.item.ItemChunkLoader;
import com.vicious.loadmychunks.item.ItemChunkometer;
import com.vicious.loadmychunks.item.ItemHasTooltip;
import com.vicious.loadmychunks.item.LMCProperties;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import com.vicious.loadmychunks.system.TickDelayer;
import com.vicious.loadmychunks.system.control.LoadState;
import com.vicious.loadmychunks.util.BoolEnum;
import com.vicious.loadmychunks.util.EnumArgument;
import com.vicious.loadmychunks.util.ModResource;
import me.shedaniel.architectury.event.events.CommandRegistrationEvent;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public class LoadMyChunks {
	public static MinecraftServer server;
	public static final String MOD_ID = "loadmychunks";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static Level debugLevel = Level.DEBUG;

	public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));

	public static final me.shedaniel.architectury.registry.Registry<Block> BLOCKS = REGISTRIES.get().get(Registry.BLOCK_REGISTRY);
	public static final me.shedaniel.architectury.registry.Registry<Item> ITEM = REGISTRIES.get().get(Registry.ITEM_REGISTRY);
	public static final me.shedaniel.architectury.registry.Registry<BlockEntityType<?>> BLOCKENTITIES = REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_REGISTRY);

	public static final Set<RegistrySupplier<Block>> chunkLoaderBlocks = new HashSet<>();
	public static Supplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;

	public static Supplier<Item> itemTickometer;
	public static Supplier<Item> itemPlayerSpoofer;
	public static Supplier<Item> itemLocatingCore;
	public static Supplier<Item> itemDiamondWire;
	public static Supplier<ItemChunkometer> itemChunkometer;

	public static CreativeModeTab creativeTab;

	public static ModResource LAG_READING_PACKET_ID = new ModResource("lag");

	public static void init() {
		logger.info("Preparing to load your chunks...");
		LMCConfig.init();
		if(LMCConfig.instance.useDebugLogging){
			logger.info("Changing to debug logging");
			debugLevel = Level.INFO;
			logger.info("Using Debug Logging");
		}
		logger.info("Creating Creative Tab.");
		creativeTab = CreativeTabs.create(new ModResource("creative_tab"),()->Registry.ITEM.get(new ModResource("chunk_loader")).getDefaultInstance());
		logger.info("Adding Chunk loader blocks");
		RegistrySupplier<Block> chunkLoaderBlock = registerCLBlockWithItem("chunk_loader", () -> {
			BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
			return new BlockChunkLoader(properties);
		});
		chunkLoaderBlocks.add(chunkLoaderBlock);

		String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
		for (String color : colors) {
			chunkLoaderBlocks.add(registerCLBlockWithItem(color + "_chunk_loader", () -> {
				BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
				return new BlockChunkLoader(properties);
			}));
		}
		CommandRegistrationEvent.EVENT.register(LoadMyChunks::registerCommands);
		itemTickometer = ITEM.register(new ModResource("tickometer"), () -> new ItemHasTooltip(new LMCProperties()));
		itemPlayerSpoofer = ITEM.register(new ModResource("player_spoofer"), () -> new ItemHasTooltip(new LMCProperties()));
		itemLocatingCore = ITEM.register(new ModResource("dimensional_locator"), () -> new ItemHasTooltip(new LMCProperties()));
		itemDiamondWire = ITEM.register(new ModResource("diamond_wire"), () -> new ItemHasTooltip(new LMCProperties()));
		itemChunkometer = ITEM.register(new ModResource("chunkometer"), () -> new ItemChunkometer(new LMCProperties()));
		if(allowUsingDebugFeatures()){
			DebugLoadMyChunks.init();
		}
		RegistryInit.BLOCKS.run();
		RegistryInit.ITEMS.run();

		LoadMyChunks.chunkLoaderBlockEntity = BLOCKENTITIES.register(new ModResource("chunk_loader"), () -> {
			Set<Block> blocks = new HashSet<>();
			for (RegistrySupplier<Block> blk : chunkLoaderBlocks) {
				blocks.add(blk.get());
			}
			return new LMCBEType<>(BlockEntityChunkLoader::new, blocks, null);
		});

		//noinspection rawtypes
		ArgumentTypes.register("lmcenum", EnumArgument.class,(ArgumentSerializer)new EnumArgument.Serializer());

		logger.info("Chunk Loader Loading Complete.");
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, LAG_READING_PACKET_ID, ((buf, context) -> {
			Player plr = context.getPlayer();
			ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level, plr.blockPosition());
			//TODO: integrate permissions with LP
			if (!LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID())) {
				cdm.timeRegardless = true;
				cdm.addRecipient((ServerPlayer) plr);
			}
		}));
	}

	public static <T extends Block> RegistrySupplier<T> registerBlockWithItem(String name, Supplier<? extends T> supplier) {
		ModResource resource = new ModResource(name);
		RegistrySupplier<T> block = (RegistrySupplier<T>) BLOCKS.register(resource, supplier);
		RegistryInit.ITEMS.queue(()->{
			ITEM.register(resource, () -> new BlockItem(block.get(), new LMCProperties()));
		});
		return block;
	}

	public static <T extends Block> RegistrySupplier<T> registerCLBlockWithItem(String name, Supplier<? extends T> supplier) {
		ModResource resource = new ModResource(name);
		RegistrySupplier<T> block = (RegistrySupplier<T>) BLOCKS.register(resource, supplier);
		RegistryInit.ITEMS.queue(()->{
			ITEM.register(resource, () -> new ItemChunkLoader(block.get(), new LMCProperties()));
		});
		return block;
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



	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("loadmychunks").requires(ctx-> ctx.hasPermission(2));
		root.then(Commands.literal("forceload").executes(ctx-> handleCMDForceload(ctx,true,null)).then(Commands.argument("permanent", EnumArgument.enumArgument(BoolEnum.class)).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",BoolEnum.class).asBoolean(),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDForceload(ctx,ctx.getArgument("permanent",BoolEnum.class).asBoolean(),BlockPosArgument.getOrLoadBlockPos(ctx,"pos"))))));
		root.then(Commands.literal("unforceload").executes(ctx-> handleCMDUnforceload(ctx,false,null)).then(Commands.argument("permanent", EnumArgument.enumArgument(BoolEnum.class)).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",BoolEnum.class).asBoolean(),null))
				.then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx-> handleCMDUnforceload(ctx,ctx.getArgument("permanent",BoolEnum.class).asBoolean(),BlockPosArgument.getOrLoadBlockPos(ctx,"pos"))))));
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
}
