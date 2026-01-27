package com.drathonix.loadmychunks.common.mixin;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import org.spongepowered.asm.mixin.Mixin;
//? if >1.16.5 {
/*import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Final;
import com.drathonix.loadmychunks.common.bridge.IEntitySectionMixin;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntitySectionStorage.class)
public class MixinEntitySectionStorage<T extends EntityAccess> {
    @Shadow @Final private Class<T> entityClass;

    @Inject(method = "createSection",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/EntitySection;<init>(Ljava/lang/Class;Lnet/minecraft/world/level/entity/Visibility;)V"),cancellable = true,locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void setCPos(long l, CallbackInfoReturnable<EntitySection<T>> cir, long m, Visibility visibility){
        EntitySection<T> sec = new EntitySection<>(this.entityClass,visibility);
        IEntitySectionMixin.setChunkPos(sec,m);
        cir.setReturnValue(sec);
    }
}
*///?} else {
@Mixin(LoadMyChunks.class)
public class MixinEntitySectionStorage {}
//?}
