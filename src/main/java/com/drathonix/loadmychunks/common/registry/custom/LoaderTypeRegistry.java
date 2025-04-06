package com.drathonix.loadmychunks.common.registry.custom;

import com.mojang.serialization.Lifecycle;
import com.drathonix.loadmychunks.common.registry.LoaderType;
import com.drathonix.loadmychunks.common.registry.LoaderTypeKeys;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.extension.PlacedExtensionChunkLoader;
import com.drathonix.loadmychunks.common.util.ModResource;
import net.minecraft.core.MappedRegistry;
//? if >1.19.4 {
/*import net.minecraft.core.RegistrationInfo;
*///?}
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Registration location for all chunk loader types.
 */
public class LoaderTypeRegistry extends MappedRegistry<LoaderType<?>> {
    public static final ResourceKey<Registry<LoaderType<?>>> KEY = ResourceKey.createRegistryKey(ModResource.of("loader_type"));
    public static final LoaderTypeRegistry INSTANCE = new LoaderTypeRegistry();

    public static final LoaderType<PlacedChunkLoader> PLACED_TYPE = register(LoaderTypeKeys.PLACED_LOADER,new LoaderType<>(PlacedChunkLoader::new));
    public static final LoaderType<PlacedExtensionChunkLoader> PLACED_EXTENSION_TYPE = register(LoaderTypeKeys.PLACED_EXTENSION_LOADER,new LoaderType<>(PlacedExtensionChunkLoader::new));

    private LoaderTypeRegistry() {
        //? if >=1.19.2 {
        super(KEY, Lifecycle.stable(),true);
        //?} else if >=1.18.2 {
        /*super(KEY, Lifecycle.stable(),null);
        *///?} else if >1.16.5 {
        /*super(KEY, Lifecycle.stable(),true);
        *///?} else {
        /*super(KEY, Lifecycle.stable());
        *///?}
    }

    /**
     * Helper method for registering a new LoaderType.
     * @param id the id of the type.
     * @param type the type instance.
     * @return type param.
     * @param <T> the chunk loader class.
     */
    public static <T extends IChunkLoader> LoaderType<T> register(ResourceLocation id, LoaderType<T> type){
        //? if >1.19.4 {
        /*INSTANCE.register(ResourceKey.create(KEY,id), type, RegistrationInfo.BUILT_IN);
        *///?} else {
        INSTANCE.register(ResourceKey.create(KEY,id), type,Lifecycle.stable());
        //?}
        return type;
    }
}
