package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "stopServer",at = @At("HEAD"))
    public void onStop(CallbackInfo ci){
        LoadMyChunks.stopping=true;
    }
}
