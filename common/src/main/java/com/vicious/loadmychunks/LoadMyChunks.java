package com.vicious.loadmychunks;

import com.mojang.brigadier.CommandDispatcher;
import com.vicious.loadmychunks.block.BlockChunkLoader;
import com.vicious.loadmychunks.block.BlockEntityChunkLoader;
import com.vicious.loadmychunks.block.LMCBEType;
import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.debug.DebugLoadMyChunks;
import com.vicious.loadmychunks.item.ItemChunkLoader;
import com.vicious.loadmychunks.item.ItemChunkometer;
import com.vicious.loadmychunks.item.ItemHasTooltip;
import com.vicious.loadmychunks.item.LMCProperties;
import com.vicious.loadmychunks.system.ChunkDataModule;
import com.vicious.loadmychunks.system.ChunkLoaderManager;
import com.vicious.loadmychunks.system.TickDelayer;
import com.vicious.loadmychunks.util.ModResource;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.impl.ItemPropertiesExtensionImpl;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadMyChunks {
	public static MinecraftServer server;
	public static final String MOD_ID = "loadmychunks";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static Level debugLevel = Level.DEBUG;

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
	public static final DeferredRegister<Item> ITEM = DeferredRegister.create(MOD_ID, Registries.ITEM);
	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(LoadMyChunks.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

	public static final Set<RegistrySupplier<Block>> chunkLoaderBlocks = new HashSet<>();
	public static RegistrySupplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;

	public static RegistrySupplier<Item> itemTickometer;
	public static RegistrySupplier<Item> itemPlayerSpoofer;
	public static RegistrySupplier<Item> itemLocatingCore;
	public static RegistrySupplier<Item> itemDiamondWire;
	public static RegistrySupplier<ItemChunkometer> itemChunkometer;

	public static RegistrySupplier<CreativeModeTab> creativeTab;

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
		DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
		creativeTab = TABS.register(new ModResource("creative_tab"),()-> CreativeTabRegistry.create(Component.translatable("loadmychunks.creativetab.title"),()->ITEM.getRegistrar().get(new ModResource("chunk_loader")).getDefaultInstance()));
		TABS.register();
		logger.info("Adding Chunk loader blocks");
		RegistrySupplier<Block> chunkLoaderBlock = registerCLBlockWithItem("chunk_loader", () -> {
			BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
			return new BlockChunkLoader(properties);
		});
		chunkLoaderBlocks.add(chunkLoaderBlock);

		String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
		for (String color : colors) {
			chunkLoaderBlocks.add(registerCLBlockWithItem(color + "_chunk_loader", () -> {
				BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
				return new BlockChunkLoader(properties);
			}));
		}
		LoadMyChunks.chunkLoaderBlockEntity = BLOCKENTITIES.register(new ModResource("chunk_loader"), () -> {
			Set<Block> blocks = new HashSet<>();
			for (RegistrySupplier<Block> blk : chunkLoaderBlocks) {
				blocks.add(blk.get());
			}
			return new LMCBEType<>(BlockEntityChunkLoader::new, blocks, null);
		});
		CommandRegistrationEvent.EVENT.register(LoadMyChunks::registerCommands);
		itemTickometer = ITEM.register(new ModResource("tickometer"), () -> new ItemHasTooltip(new LMCProperties()));
		itemPlayerSpoofer = ITEM.register(new ModResource("player_spoofer"), () -> new ItemHasTooltip(new LMCProperties()));
		itemLocatingCore = ITEM.register(new ModResource("dimensional_locator"), () -> new ItemHasTooltip(new LMCProperties()));
		itemDiamondWire = ITEM.register(new ModResource("diamond_wire"), () -> new ItemHasTooltip(new LMCProperties()));
		itemChunkometer = ITEM.register(new ModResource("chunkometer"), () -> new ItemChunkometer(new LMCProperties()));
		if(allowUsingDebugFeatures()){
			DebugLoadMyChunks.init();
		}
		ITEM.register();
		BLOCKS.register();
		BLOCKENTITIES.register();

		logger.info("Chunk Loader Loading Complete.");
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, LAG_READING_PACKET_ID, ((buf, context) -> {
			Player plr = context.getPlayer();
			ChunkDataModule cdm = ChunkLoaderManager.getOrCreateChunkData((ServerLevel) plr.level(), plr.blockPosition());
			//TODO: integrate permissions with LP
			if (!LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID())) {
				cdm.timeRegardless = true;
				cdm.addRecipient((ServerPlayer) plr);
				//TickDelayer.delayOneTick(() -> {
				//	FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
				//	newBuf.writeFloat(cdm.getTickTimer().getLagFraction());
				//	NetworkManager.sendToPlayer((ServerPlayer) plr, LAG_READING_PACKET_ID, newBuf);
				//});
			}
		}));
	}

	public static <T extends Block> RegistrySupplier<T> registerBlockWithItem(String name, Supplier<? extends T> supplier) {
		ModResource resource = new ModResource(name);
		RegistrySupplier<T> block = BLOCKS.register(resource, supplier);
		ITEM.register(resource, () -> new BlockItem(block.get(), new LMCProperties()));
		return block;
	}

	public static <T extends Block> RegistrySupplier<T> registerCLBlockWithItem(String name, Supplier<? extends T> supplier) {
		ModResource resource = new ModResource(name);
		RegistrySupplier<T> block = BLOCKS.register(resource, supplier);
		ITEM.register(resource, () -> new ItemChunkLoader(block.get(), new LMCProperties()));
		return block;
	}

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {

	}

	public static void serverStarted(MinecraftServer server) {
		LoadMyChunks.server = server;
		server.addTickable(TickDelayer::tick);
	}

	public static void serverStopped(MinecraftServer server) {
		ChunkLoaderManager.clear();
	}

	public static boolean allowUsingDebugFeatures() {
		return false;
	}
}
