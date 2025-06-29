//? if >1.21.3 {
/*package com.drathonix.loadmychunks.client;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class LagProperty implements RangeSelectItemModelProperty {
    public static final MapCodec<LagProperty> MAP_CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.empty();
        }

        @Override
        public <T> DataResult<LagProperty> decode(DynamicOps<T> ops, MapLike<T> input) {
            return DataResult.success(new LagProperty());
        }

        @Override
        public <T> RecordBuilder<T> encode(LagProperty input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return new RecordBuilder.MapBuilder<>(ops);
        }
    };
    public static final LagProperty INSTANCE = new LagProperty();

    @Override
    public float get(ItemStack arg, @Nullable ClientLevel arg2, @Nullable LivingEntity arg3, int i) {
        return LoadMyChunksClient.lagLevel;
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
*///?}
