//? if >1.21.3 {
/*package com.drathonix.loadmychunks.client.bridge;

import com.drathonix.loadmychunks.client.LagProperty;
import com.drathonix.loadmychunks.common.util.ModResource;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class RangeSelectItemModelPropertiesRegister {
    public static void init(ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>> mapper){
        MAPPER = mapper;
        RangeSelectItemModelPropertiesRegister.register(ModResource.of("lag"),LagProperty.INSTANCE.type());
    }
    static void register(ResourceLocation id, MapCodec<? extends RangeSelectItemModelProperty> instance) {
        MAPPER.put(id,instance);
    }

    public static ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>> MAPPER;
}
*///?}
