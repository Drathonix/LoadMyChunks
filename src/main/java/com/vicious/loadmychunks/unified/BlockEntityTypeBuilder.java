package com.vicious.loadmychunks.unified;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//? if fabric {
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
public class BlockEntityTypeBuilder {
    public static <T extends BlockEntity> BlockEntityType<T> build(FabricBlockEntityTypeBuilder.Factory<T> factory, Collection<Block> blocks){
        return FabricBlockEntityTypeBuilder.create(factory,blocks.toArray(new Block[0])).build();
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory,blocks).build();
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(FabricBlockEntityTypeBuilder.Factory<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return FabricBlockEntityTypeBuilder.create(factory,blockArr).build();
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(FabricBlockEntityTypeBuilder.Factory<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return FabricBlockEntityTypeBuilder.create(factory,blockArr).build();
    }
}
//?}

//? if forge {
/*import net.minecraft.world.level.block.entity.BlockEntityType;
public class BlockEntityTypeBuilder {
    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Collection<Block> blocks){
        return BlockEntityType.Builder.of(factory,blocks.toArray(new Block[0])).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory,blocks).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return BlockEntityType.Builder.of(factory,blockArr).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return BlockEntityType.Builder.of(factory,blockArr).build(null);
    }
}
*///?}

//? if neoforge {
/*import net.minecraft.world.level.block.entity.BlockEntityType;
public class BlockEntityTypeBuilder {
    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Collection<Block> blocks){
        //? if <1.21.1
        return new BlockEntityType<>(factory,blocks.toArray(new Block[0]));
        //? if >1.21
        return new BlockEntityType<>(factory, new HashSet<>(blocks),null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        //? if <1.21.1
        return new BlockEntityType<>(factory,blocks);
        //? if >1.21
        return new BlockEntityType<>(factory, Set.of(blocks),null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        //? if <1.21.1
        return new BlockEntityType<>(factory,blockArr);
        //? if >1.21
        return new BlockEntityType<>(factory, Set.of(blockArr),null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        //? if <1.21.1
        return new BlockEntityType<>(factory,blockArr);
        //? if >1.21
        return new BlockEntityType<>(factory, Set.of(blockArr),null);
    }
}
*///?}