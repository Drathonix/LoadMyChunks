package com.vicious.loadmychunks.mixins;

import com.mojang.brigadier.arguments.ArgumentType;
import com.vicious.loadmychunks.bridge.IMixinArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArgumentTypeInfos.class)
public abstract class MixinArgumentTypeInfos implements IMixinArgumentTypeInfos {
    @Shadow
    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> arg, String string, Class<? extends A> class_, ArgumentTypeInfo<A, T> arg2) {
        return null;
    }

    @Unique
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> lmc$register(String string, Object class_, Object info) {
        return register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE,string,(Class<A>)class_,(ArgumentTypeInfo<A, T>)info);
    }
}
