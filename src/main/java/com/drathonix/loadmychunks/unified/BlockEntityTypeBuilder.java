package com.drathonix.loadmychunks.unified;

//? if >1.16.5 {
/*import dev.architectury.registry.registries.RegistrySupplier;
*///?} else {
import me.shedaniel.architectury.registry.RegistrySupplier;
//?}
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.function.Supplier;

//? if fabric {
//? >1.16.5 {
/*import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
*///?}

public class BlockEntityTypeBuilder {
    //? if >1.16.5 {
    /*public static <T extends BlockEntity> BlockEntityType<T> build(FabricBlockEntityTypeBuilder.Factory<T> factory, Collection<Block> blocks){
        return build(factory,blocks.toArray(new Block[0]));
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory,blocks).build();
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(FabricBlockEntityTypeBuilder.Factory<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return build(factory,blockArr);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(FabricBlockEntityTypeBuilder.Factory<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return build(factory,blockArr);
    }
    *///?} else {
    public static <T extends BlockEntity> BlockEntityType<T> build(Supplier<T> factory, Collection<Block> blocks){
        return build(factory,blocks.toArray(new Block[0]));
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(Supplier<T> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory,blocks).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(Supplier<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return build(factory,blockArr);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(Supplier<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return build(factory,blockArr);
    }
    //?}
}
//?} elif forge {
/*public class BlockEntityTypeBuilder {
    //? if >1.16.5 {
    /^public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Collection<Block> blocks){
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
    ^///?} else {
    public static <T extends BlockEntity> BlockEntityType<T> build(Supplier<T> factory, Collection<Block> blocks){
        return BlockEntityType.Builder.of(factory,blocks.toArray(new Block[0])).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(Supplier<T> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory,blocks).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(Supplier<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return BlockEntityType.Builder.of(factory,blockArr).build(null);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(Supplier<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return BlockEntityType.Builder.of(factory,blockArr).build(null);
    }
    //?}
}
*///?} elif neoforge {
/*public class BlockEntityTypeBuilder {
    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Collection<Block> blocks){
        return build(factory, blocks.toArray(new Block[0]));
    }

    public static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        //? >1.20.5 && !=1.20.6 && !=1.21 && !=1.21.1 {
        /^return new BlockEntityType<>(factory,blocks);
        ^///?} else {
        return new BlockEntityType<>(factory, Set.of(blocks),null);
        //?}
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, RegistrySupplier<Block>... blocks) {
        Block[] blockArr = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blockArr[i] = blocks[i].get();
        }
        return build(factory,blockArr);
    }

    public static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntitySupplier<T> factory, Collection<RegistrySupplier<Block>> blocks) {
        Block[] blockArr = new Block[blocks.size()];
        int i = 0;
        for (RegistrySupplier<Block> block : blocks) {
            blockArr[i] = block.get();
            i++;
        }
        return build(factory,blockArr);
    }
}
*///?}