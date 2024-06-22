package com.vicious.loadmychunks.registry;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.block.BlockChunkLoader;
import com.vicious.loadmychunks.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.block.blockentity.LMCBEType;
import com.vicious.loadmychunks.item.ItemChunkLoader;
import com.vicious.loadmychunks.item.ItemChunkometer;
import com.vicious.loadmychunks.item.ItemHasTooltip;
import com.vicious.loadmychunks.item.LMCProperties;
import com.vicious.loadmychunks.util.ModResource;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class LMCContent {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(LoadMyChunks.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final Set<RegistrySupplier<Block>> chunkLoaderBlocks = new HashSet<>();
    public static RegistrySupplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;

    public static RegistrySupplier<Item> itemTickometer;
    public static RegistrySupplier<Item> itemPlayerSpoofer;
    public static RegistrySupplier<Item> itemLocatingCore;
    public static RegistrySupplier<Item> itemDiamondWire;
    public static RegistrySupplier<ItemChunkometer> itemChunkometer;

    public static RegistrySupplier<CreativeModeTab> creativeTab;

    public static void init() {
        creativeTab = TABS.register(ModResource.of("creative_tab"),()-> CreativeTabRegistry.create(Component.translatable("loadmychunks.creativetab.title"),()->LMCRegistrar.ITEM.getRegistrar().get(ModResource.of("chunk_loader")).getDefaultInstance()));
        TABS.register();
        LMCRegistrar.BLOCK.queue(reg->{
            RegistrySupplier<Block> chunkLoaderBlock = registerCLBlockWithItem(reg,"chunk_loader", () -> {
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                return new BlockChunkLoader(properties);
            });
            String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
            for (String color : colors) {
                chunkLoaderBlocks.add(registerCLBlockWithItem(reg,color + "_chunk_loader", () -> {
                    BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                    return new BlockChunkLoader(properties);
                }));
            }
            chunkLoaderBlocks.add(chunkLoaderBlock);
        });
        LMCRegistrar.ITEM.queue(reg->{
            itemTickometer = reg.register(ModResource.of("tickometer"), () -> new ItemHasTooltip(new LMCProperties()));
            itemPlayerSpoofer = reg.register(ModResource.of("player_spoofer"), () -> new ItemHasTooltip(new LMCProperties()));
            itemLocatingCore = reg.register(ModResource.of("dimensional_locator"), () -> new ItemHasTooltip(new LMCProperties()));
            itemDiamondWire = reg.register(ModResource.of("diamond_wire"), () -> new ItemHasTooltip(new LMCProperties()));
            itemChunkometer = reg.register(ModResource.of("chunkometer"), () -> new ItemChunkometer(new LMCProperties()));

        });

        LMCRegistrar.BLOCK_ENTITY_TYPE.queue(reg->{
            chunkLoaderBlockEntity = reg.register(ModResource.of("chunk_loader"), () -> {
                Set<Block> blocks = new HashSet<>();
                for (RegistrySupplier<Block> blk : chunkLoaderBlocks) {
                    blocks.add(blk.get());
                }
                return new LMCBEType<>(BlockEntityChunkLoader::new, blocks, null);
            });
        });

        LMCRegistrar.init();
    }

    public static <T extends Block> RegistrySupplier<T> registerBlockWithItem(DeferredRegister<Block> reg, String name, Supplier<? extends T> supplier) {
        ResourceLocation resource = ModResource.of(name);
        RegistrySupplier<T> block = reg.register(resource, supplier);
        LMCRegistrar.ITEM.queue(ireg->{
            ireg.register(resource, () -> new BlockItem(block.get(), new LMCProperties()));
        });
        return block;
    }

    public static <T extends Block> RegistrySupplier<T> registerCLBlockWithItem(DeferredRegister<Block> reg, String name, Supplier<? extends T> supplier) {
        ResourceLocation resource = ModResource.of(name);
        RegistrySupplier<T> block = reg.register(resource, supplier);
        LMCRegistrar.ITEM.queue(ireg->{
            ireg.register(resource, () -> new ItemChunkLoader(block.get(), new LMCProperties()));
        });
        return block;
    }
}
