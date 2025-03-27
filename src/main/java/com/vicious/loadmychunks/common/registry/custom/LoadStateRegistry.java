package com.vicious.loadmychunks.common.registry.custom;

import com.mojang.serialization.Lifecycle;
import com.vicious.loadmychunks.common.registry.LoaderType;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.system.control.LoadStates;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.util.ModResource;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * Maintains the list of all possible loadstates.
 */
public class LoadStateRegistry extends MappedRegistry<LoadStates.ILoadState> {
    public static final ResourceKey<Registry<LoadStates.ILoadState>> KEY = ResourceKey.createRegistryKey(ModResource.of("loader_state"));
    public static final LoadStateRegistry INSTANCE = new LoadStateRegistry();

    private LoadStateRegistry() {
        super(KEY, Lifecycle.stable(),true);
    }

    public static <T extends LoadStates.ILoadState> T register(ResourceLocation id, T type){
        INSTANCE.register(ResourceKey.create(KEY,id), type, RegistrationInfo.BUILT_IN);
        return type;
    }
}
