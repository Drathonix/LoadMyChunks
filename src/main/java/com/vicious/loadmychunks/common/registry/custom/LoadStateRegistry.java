package com.vicious.loadmychunks.common.registry.custom;

import com.mojang.serialization.Lifecycle;
import com.vicious.loadmychunks.common.system.control.ILoadState;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.system.control.LoaderPower;
import com.vicious.loadmychunks.common.util.ModResource;
import dev.architectury.platform.Mod;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Maintains the list of all possible load states. You likely do not need to add any others.
 */
public class LoadStateRegistry extends MappedRegistry<ILoadState> {
    private static final List<ILoadState> statesByIndex = new ArrayList<>();

    public static final ResourceKey<Registry<ILoadState>> KEY = ResourceKey.createRegistryKey(ModResource.of("loader_state"));
    public static final LoadStateRegistry INSTANCE = new LoadStateRegistry();

    public static final ILoadState DISABLED = LoadStateEnum.DISABLED;
    public static final ILoadState TICKING = LoadStateEnum.TICKING;
    public static final ILoadState ENTITY_TICKING;
    public static final ILoadState OVERTICKED = LoadStateEnum.OVERTICKED;
    public static final ILoadState PERMANENT = LoadStateEnum.PERMANENT;
    public static final ILoadState PERMANENTLY_DISABLED = LoadStateEnum.PERMANENTLY_DISABLED;

    private LoadStateRegistry() {
        super(KEY, Lifecycle.stable(),true);
    }

    private static ILoadState register(ResourceLocation id, ILoadState type){
        INSTANCE.register(ResourceKey.create(KEY,id), type, RegistrationInfo.BUILT_IN);
        return type;
    }

    /**
     * Registers a load state by construction.
     * @param key the load state's unique key.
     * @param stateFactory takes in the load state's int id. Implementers should return this using {@link ILoadState#id()}
     */
    public static ILoadState registerLoadState(ResourceLocation key, Function<Integer, ILoadState> stateFactory){
        return registerLoadState(key,stateFactory.apply(getNextOpenId()));
    }

    private static ILoadState registerLoadState(ResourceLocation key, ILoadState state){
        statesByIndex.add(register(key,state));
        return state;
    }

    private static int getNextOpenId(){
        return statesByIndex.size();
    }

    /**
     * Safely gets a ILoadState from an nbt compound.
     * @param key the compound key
     * @param tag the compound tag
     * @param defaultState a default nonnull state.
     * @return the decoded state or default state.
     */
    public static ILoadState fromCompound(@NotNull String key, @NotNull CompoundTag tag, @NotNull ILoadState defaultState){
        if(tag.contains(key, Tag.TAG_INT)){
            int k = tag.getInt(key);
            if(statesByIndex.size() < k || k < 0){
                return defaultState;
            }
            return statesByIndex.get(k);
        }
        if(tag.contains(key, Tag.TAG_STRING)){
            Optional<ResourceLocation> k = Optional.ofNullable(ModResource.parse(tag.getString(key)));
            return k.map(l->INSTANCE.getOptional(l).orElse(defaultState)).orElse(defaultState);
        }
        return defaultState;
    }

    static {
        for (LoadStateEnum value : LoadStateEnum.values()) {
            registerLoadState(value.getResourceLocation(),value);
        }
        ENTITY_TICKING = registerLoadState(ModResource.of("entity_ticking"), id->new ILoadState() {
            @Override
            public LoaderPower blockEntityTickingPower() {
                return LoaderPower.FORCED_MANAGED;
            }

            @Override
            public LoaderPower entityForcingPower() {
                return LoaderPower.FORCED_MANAGED;
            }

            @Override
            public int id() {
                return id;
            }
        });
    }
}
