package com.vicious.loadmychunks.common.registry;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.block.BlockChunkLoader;
import com.vicious.loadmychunks.common.block.BlockLagometer;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.vicious.loadmychunks.common.block.blockentity.LMCBEType;
import com.vicious.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.vicious.loadmychunks.common.debug.DebugLoadMyChunks;
import com.vicious.loadmychunks.common.item.*;
import com.vicious.loadmychunks.common.util.ModResource;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
*///?}
//? if >1.16.5 {
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
//?}
//? if >1.19.5
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? if <1.19.5
/*import net.minecraft.world.level.material.Material;*/

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LMCContent {
    //? if >1.20.0
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(LoadMyChunks.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final Set<RegistrySupplier<Block>> chunkLoaderBlocks = new HashSet<>();
    public static RegistrySupplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;
    public static RegistrySupplier<BlockEntityType<BlockEntityLagometer>> lagometerBlockEntity;


    public static RegistrySupplier<Item> itemTickometer;
    public static RegistrySupplier<Item> itemPlayerSpoofer;
    public static RegistrySupplier<Item> itemLocatingCore;
    public static RegistrySupplier<Item> itemDiamondWire;
    public static RegistrySupplier<ItemChunkometer> itemChunkometer;
    public static RegistrySupplier<Block> lagometerBlock;
    public static RegistrySupplier<Block> chunkLoaderBlock;

    //? if <=1.19.3 || >1.19.4
    public static RegistrySupplier<CreativeModeTab> creativeTab;
    //? if >1.19.3 && <1.19.5
    /*public static CreativeTabRegistry.TabSupplier creativeTab;*/

    public static void init() {
        //? if <=1.18.1
        /*creativeTab = new FakeRegistrySupplier<>(CreativeTabs.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));*/
        //? if <1.19.3 && >1.18.1
        /*creativeTab = new FakeRegistrySupplier<>(CreativeTabRegistry.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));*/
        //? if >1.19.3 && <1.19.5
        /*creativeTab = CreativeTabRegistry.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance());*/
        //? if >1.20.0 {
        creativeTab = TABS.register(ModResource.of("creative_tab"),()-> CreativeTabRegistry.create(Component.translatable("loadmychunks.creativetab.title"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));
        TABS.register();
        //?}
        LMCRegistrar.BLOCK.queue(reg->{
            chunkLoaderBlock = registerCLBlockWithItem(reg,"chunk_loader", () -> {
                //TODO: find a better way to abstract this somehow.
                //? if <1.19.5
                /*BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);*/
                //? if >1.19.4
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                return new BlockChunkLoader(properties);
            });
            String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
            for (String color : colors) {
                chunkLoaderBlocks.add(registerCLBlockWithItem(reg,color + "_chunk_loader", () -> {
                    //? if <1.19.5
                    /*BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);*/
                    //? if >1.19.4
                    BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(50.0F, 1200.0F);
                    return new BlockChunkLoader(properties);
                }));
            }
            chunkLoaderBlocks.add(chunkLoaderBlock);
            lagometerBlock = registerBlockWithItem(reg,"lagometer",()->{
                //? if <1.19.5
                /*BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F);*/
                //? if >1.19.4
                BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F);
                return new BlockLagometer(properties);
            }, ItemLagometer::new);
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
            lagometerBlockEntity = reg.register(ModResource.of("lagometer"), ()->{
                Set<Block> set = new HashSet<>();
                set.add(lagometerBlock.get());
                return new LMCBEType<>(BlockEntityLagometer::new, set, null);
            });
        });

        if(LoadMyChunks.allowUsingDebugFeatures()){
            DebugLoadMyChunks.init();
        }

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

    public static <T extends Block> RegistrySupplier<T> registerBlockWithItem(DeferredRegister<Block> reg, String name, Supplier<? extends T> supplier, BiFunction<Block, LMCProperties, BlockItem> function) {
        ResourceLocation resource = ModResource.of(name);
        RegistrySupplier<T> block = reg.register(resource, supplier);
        LMCRegistrar.ITEM.queue(ireg->{
            ireg.register(resource, () -> function.apply(block.get(),new LMCProperties()));
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