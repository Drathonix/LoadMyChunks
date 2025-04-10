package com.drathonix.loadmychunks.common.registry;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import com.drathonix.loadmychunks.common.block.BlockChunkLoader;
import com.drathonix.loadmychunks.common.block.BlockLagometer;
import com.drathonix.loadmychunks.common.block.blockentity.BlockEntityLagometer;
import com.drathonix.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.drathonix.loadmychunks.common.debug.LoadMyChunksDebug;
import com.drathonix.loadmychunks.common.item.*;
import com.drathonix.loadmychunks.common.util.ModResource;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import com.drathonix.loadmychunks.unified.BlockEntityTypeBuilder;
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
/*import net.minecraft.core.registries.Registries;*/
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
//? if <1.19.5
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LMCContent {
    //? if >1.19.4 {
    /*private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(LoadMyChunks.MOD_ID, Registries.CREATIVE_MODE_TAB);
    *///?}
    public static final Map<String,RegistrySupplier<Block>> chunkLoaderBlockMap = new HashMap<>();
    public static RegistrySupplier<BlockEntityType<BlockEntityChunkLoader>> chunkLoaderBlockEntity;
    public static RegistrySupplier<BlockEntityType<BlockEntityLagometer>> lagometerBlockEntity;


    public static RegistrySupplier<Item> itemTickometer;
    public static RegistrySupplier<Item> itemPlayerSpoofer;
    public static RegistrySupplier<Item> itemLifeforceBroadcaster;
    public static RegistrySupplier<Item> itemLocatingCore;
    public static RegistrySupplier<Item> itemDiamondWire;
    public static RegistrySupplier<ItemChunkLoaderExtension> itemExtension;
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
        creativeTab = new FakeRegistrySupplier<>(CreativeTabRegistry.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));
        //? if >1.19.3 && <1.19.5
        /*creativeTab = CreativeTabRegistry.create(ModResource.of("creative_tab"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance());*/
        //? if >1.20.0 {
        /*creativeTab = TABS.register(ModResource.of("creative_tab"),()-> CreativeTabRegistry.create(Component.translatable("loadmychunks.creativetab.title"),()->LMCRegistrar.ITEM.get(ModResource.of("chunk_loader")).getDefaultInstance()));
        TABS.register();
        *///?}
        LMCRegistrar.BLOCK.queue(reg->{
            chunkLoaderBlock = registerCLBlockWithItem(reg,"chunk_loader", () -> {
                return new BlockChunkLoader(MultiversioningHelper.properties(50f,1200f));
            });
            String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
            for (String color : colors) {
                RegistrySupplier<Block> block = registerCLBlockWithItem(reg,color + "_chunk_loader", () -> {
                    return new BlockChunkLoader(MultiversioningHelper.properties(50f,1200f));
                });
                chunkLoaderBlockMap.put(color, block);
            }
            chunkLoaderBlockMap.put("",chunkLoaderBlock);
            lagometerBlock = registerBlockWithItem(reg,"lagometer",()->{
                return new BlockLagometer(MultiversioningHelper.properties(3.5f,4f));
            }, ItemLagometer::new);
        });
        LMCRegistrar.ITEM.queue(reg->{
            itemTickometer = reg.register(ModResource.of("tickometer"), () -> new ItemHasTooltip(new LMCProperties(),1));
            itemPlayerSpoofer = reg.register(ModResource.of("player_spoofer"), () -> new ItemHasTooltip(new LMCProperties(),3));
            itemLocatingCore = reg.register(ModResource.of("dimensional_locator"), () -> new ItemHasTooltip(new LMCProperties(),2));
            itemDiamondWire = reg.register(ModResource.of("diamond_wire"), () -> new ItemHasTooltip(new LMCProperties(),3));
            itemChunkometer = reg.register(ModResource.of("chunkometer"), () -> new ItemChunkometer(new LMCProperties()));
            itemExtension = reg.register(ModResource.of("chunk_loader_extension"), () -> new ItemChunkLoaderExtension(new LMCProperties()));
            itemLifeforceBroadcaster = reg.register(ModResource.of("lifeforce_broadcaster"), () -> new ItemLifeforceBroadcaster(new LMCProperties()));
        });

        LMCRegistrar.BLOCK_ENTITY_TYPE.queue(reg->{
            chunkLoaderBlockEntity = reg.register(ModResource.of("chunk_loader"), () -> BlockEntityTypeBuilder.make(BlockEntityChunkLoader::new,chunkLoaderBlockMap.values()));
            lagometerBlockEntity = reg.register(ModResource.of("lagometer"), ()-> BlockEntityTypeBuilder.make(BlockEntityLagometer::new, lagometerBlock));
        });

        if(LoadMyChunks.allowUsingDebugFeatures()){
            LoadMyChunksDebug.init();
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