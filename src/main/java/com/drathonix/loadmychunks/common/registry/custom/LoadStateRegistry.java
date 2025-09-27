package com.drathonix.loadmychunks.common.registry.custom;

import com.mojang.serialization.Lifecycle;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.control.LoaderPower;
import com.drathonix.loadmychunks.common.util.ModResource;
//? if >1.16.5 {
import net.minecraft.core.Holder;
//?}
//? if >1.20.4 {
/*import net.minecraft.core.RegistrationInfo;
*///?}
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
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
    public static final ILoadState ENTITY_TICKING_PERMANENT;
    public static final ILoadState OVERTICKED = LoadStateEnum.OVERTICKED;
    public static final ILoadState PERMANENT = LoadStateEnum.PERMANENT;
    public static final ILoadState PERMANENTLY_DISABLED = LoadStateEnum.PERMANENTLY_DISABLED;

    private LoadStateRegistry() {
        //? if >=1.19.4 {
        /*super(KEY, Lifecycle.stable(),true);
        *///?} else if >=1.18.2 {
        super(KEY, Lifecycle.stable(), ILoadState::getIntrusiveHolder);
        //?} else if >1.16.5 {
        /*super(KEY, Lifecycle.stable(),true);
         *///?} else {
        /*super(KEY, Lifecycle.stable());
         *///?}
    }

    private static ILoadState register(ResourceLocation id, ILoadState type){
        //? if >1.20.4 {
        /*INSTANCE.register(ResourceKey.create(KEY,id), type, RegistrationInfo.BUILT_IN);
        *///?} else {
        INSTANCE.register(ResourceKey.create(KEY,id), type,Lifecycle.stable());
        //?}
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
        if(tag.contains(key, 3)){
            int k = tag.getInt(key);
            if(statesByIndex.size() < k || k < 0){
                return defaultState;
            }
            return statesByIndex.get(k);
        }
        if(tag.contains(key, 8)){
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
            //? if >1.16.5 {
            private final Holder.Reference<ILoadState> holder = LoadStateRegistry.INSTANCE.createIntrusiveHolder(this);
            @Override
            public Holder.Reference<ILoadState> getIntrusiveHolder() {
                return holder;
            }
            //?}

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

        ENTITY_TICKING_PERMANENT = registerLoadState(ModResource.of("entity_ticking_permanent"), id->new ILoadState() {
            //? if >1.16.5 {
            private final Holder.Reference<ILoadState> holder = LoadStateRegistry.INSTANCE.createIntrusiveHolder(this);
            @Override
            public Holder.Reference<ILoadState> getIntrusiveHolder() {
                return holder;
            }
            //?}

            @Override
            public LoaderPower blockEntityTickingPower() {
                return LoaderPower.FORCED;
            }

            @Override
            public LoaderPower entityForcingPower() {
                return LoaderPower.FORCED;
            }

            @Override
            public int id() {
                return id;
            }
        });


    }
}
