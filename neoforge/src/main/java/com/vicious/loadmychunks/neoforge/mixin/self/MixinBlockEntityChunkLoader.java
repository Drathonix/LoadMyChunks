package com.vicious.loadmychunks.neoforge.mixin.self;

import com.vicious.loadmychunks.block.BlockEntityChunkLoader;
import com.vicious.loadmychunks.integ.Integrations;
import com.vicious.loadmychunks.integ.cct.ChunkLoaderPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Mixin(BlockEntityChunkLoader.class)
public class MixinBlockEntityChunkLoader implements ICapabilityProvider<Object,Object,Object> {
    @Shadow
    @Final
    private final Map<Object, Supplier<Object>> capabilities = new HashMap<>();

    @Inject(method = "setLevel",at = @At("RETURN"))
    public void injectCapabilities(CallbackInfo ci){
        if(!BlockEntityChunkLoader.class.cast(this).getLevel().isClientSide()) {
            if (Integrations.cctPresent) {
                capabilities.put(PeripheralCapability.get(), () -> new ChunkLoaderPeripheral(BlockEntityChunkLoader.class.cast(this)));
            }
        }
    }


    @Nullable
    @Override
    public Object getCapability(Object object, Object context) {
        return capabilities.get(object);
    }
}
