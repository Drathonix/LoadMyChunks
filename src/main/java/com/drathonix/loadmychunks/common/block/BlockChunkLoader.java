package com.drathonix.loadmychunks.common.block;


import com.mojang.serialization.MapCodec;
import com.drathonix.loadmychunks.common.block.blockentity.BlockEntityChunkLoader;
import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.registry.LMCContent;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.loaders.IHasChunkloader;
import com.drathonix.loadmychunks.common.util.Message;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockChunkLoader extends BaseEntityBlock {
    //? if >1.20.3 {
    public static final MapCodec<BlockChunkLoader> CODEC = simpleCodec(BlockChunkLoader::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    public BlockChunkLoader(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if(livingEntity instanceof ServerPlayer){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof BlockEntityChunkLoader) {
                ((BlockEntityChunkLoader) entity).setOwner(livingEntity.getUUID());
                if(ChunkDataManager.hasExceededOwnershipCap(livingEntity.getUUID())){
                    Message.send((ServerPlayer) livingEntity,Message.styled(Message.translatable("loadmychunks.message.too_many_loaded_chunks", LMCConfig.limitSettings.limit,ChunkDataManager.getCountLoadedChunksOf(livingEntity.getUUID())), ChatFormatting.RED,true,true));
                }
            }
        }
    }

    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        BlockEntityChunkLoader blockEntity = (BlockEntityChunkLoader) level.getBlockEntity(blockPos);
        IHasChunkloader.ifPresent(blockEntity,loader->{
            int k = loader.getExtensionRange();
            NonNullList<ItemStack> out = NonNullList.create();
            while(k > 0){
                int j = Math.min(64,k);
                k-=j;
                out.add(new ItemStack(LMCContent.itemExtension.get(),j));
            }
            if(loader.getDefaultState() == LoadStateRegistry.ENTITY_TICKING) {
                out.add(new ItemStack(LMCContent.itemLifeforceBroadcaster.get()));
            }
            Containers.dropContents(level,blockPos, out);
        });
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    /*@Override
    public @NotNull ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = super.getCloneItemStack(levelReader, blockPos, blockState);
        levelReader.getBlockEntity(blockPos, LMCContent.chunkLoaderBlockEntity.get()).ifPresent((loader) -> {
            loader.saveToItem(itemStack, levelReader.registryAccess());
        });
        return itemStack;
    }*/

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    //? if >1.16.5 {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityChunkLoader(blockPos,blockState);
    }
    //?}

    //? if <=1.16.5 {
    /*@Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new BlockEntityChunkLoader();
    }
    *///?}
}
