package com.vicious.loadmychunks.common.registry;

import com.vicious.loadmychunks.common.LoadMyChunks;
//? if <=1.16.5 {
import com.vicious.loadmychunks.common.block.BlockChunkLoader;
import com.vicious.loadmychunks.common.block.BlockLagometer;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.block.blockentity.LMCBEType;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.common.item.ItemChunkLoader;
import com.vicious.loadmychunks.common.item.ItemChunkometer;
import com.vicious.loadmychunks.common.item.ItemHasTooltip;
import com.vicious.loadmychunks.common.item.LMCProperties;
import com.vicious.loadmychunks.common.util.ModResource;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
//}
//? if >1.16.5 {
/*import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
*///?}
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class LMCContent {
    //? if >=1.20.1
    /*private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(LoadMyChunks.MOD_ID, Registry.CREATIVE_MODE_TAB);*/
    public static final Set<RegistrySupplier<Block>> chunkLoaderBlocks = new HashSet<>();
    public static RegistrySupplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;
    public static RegistrySupplier<BlockEntityType<BlockEntityLagometer>> lagometerBlockEntity;

    public static RegistrySupplier<Item> itemTickometer;
    public static RegistrySupplier<Item> itemPlayerSpoofer;
    public static RegistrySupplier<Item> itemLocatingCore;
    public static RegistrySupplier<Item> itemDiamondWire;
    public static RegistrySupplier<Block> lagometerBlock;
    public static RegistrySupplier<ItemChunkometer> itemChunkometer;

    public static RegistrySupplier<CreativeModeTab> creativeTab;

    public static void init() {
        //? if <=1.16.5 {
        creativeTab = new FakeRegistrySupplier<>(CreativeTabs.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));
        //?}
        //? if >=1.20.1 {
        /*creativeTab = TABS.register(ModResource.of("creative_tab"),()-> CreativeTabRegistry.create(Component.translatable("loadmychunks.creativetab.title"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));
        TABS.register();
        *///?}
        LMCRegistrar.BLOCK.queue(reg->{
            RegistrySupplier<Block> chunkLoaderBlock = registerCLBlockWithItem(reg,"chunk_loader", () -> {
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                return new BlockChunkLoader(properties);
            });
            String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
            for (String color : colors) {
                chunkLoaderBlocks.add(registerCLBlockWithItem(reg,color + "_chunk_loader", () -> {
                    BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                    return new BlockChunkLoader(properties);
                }));
            }
            chunkLoaderBlocks.add(chunkLoaderBlock);
            lagometerBlock = registerBlockWithItem(reg,"lagometer",()->{
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F);
                return new BlockLagometer(properties);
            });
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
            Set<Block> set = new HashSet<>();
            set.add(lagometerBlock.get());
            lagometerBlockEntity = reg.register(ModResource.of("lagometer"), ()->new LMCBEType<>(BlockEntityLagometer::new, set, null));
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