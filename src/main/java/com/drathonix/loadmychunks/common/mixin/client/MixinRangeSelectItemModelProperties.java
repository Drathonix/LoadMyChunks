package com.drathonix.loadmychunks.common.mixin.client;
import org.spongepowered.asm.mixin.Mixin;

//? if >1.21.3 {
/*import com.drathonix.loadmychunks.client.bridge.RangeSelectItemModelPropertiesRegister;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangeSelectItemModelProperties.class)
public class MixinRangeSelectItemModelProperties {
    @Shadow @Final
    private static ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>> ID_MAPPER;
    @Inject(method = "bootstrap",at = @At("TAIL"))
    private static void postBootstrap(CallbackInfo ci) {
        RangeSelectItemModelPropertiesRegister.init(ID_MAPPER);
    }
}
*///?} else {
import com.drathonix.loadmychunks.common.LoadMyChunks;
@Mixin(LoadMyChunks.class)
public class MixinRangeSelectItemModelProperties {

}
//?}

