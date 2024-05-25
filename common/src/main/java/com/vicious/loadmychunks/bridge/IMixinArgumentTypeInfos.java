package com.vicious.loadmychunks.bridge;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;

public interface IMixinArgumentTypeInfos {
    <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> lmc$register(String string, Object class_, Object info);
}
