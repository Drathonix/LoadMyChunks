package com.drathonix.loadmychunks.common.system.control;

import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.util.ModResource;
//? if >1.16.5 {
import net.minecraft.core.Holder;
//?}
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Old implementation of {@link ILoadState} that is missing {@link LoadStateRegistry#ENTITY_TICKING} and {@link LoadStateRegistry#ENTITY_TICKING_PERMANENT}
 * These instances are registered before all others.
 * @since 1.0.0
 * @author Jack Andersen
 */
public enum LoadStateEnum implements ILoadState {
    DISABLED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    },
    TICKING,
    OVERTICKED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    },
    PERMANENT{
        @Override
        public boolean permanent() {
            return true;
        }
    },
    PERMANENTLY_DISABLED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    };

    //? if >1.16.5 {
    /**
     * Holder instance is being stored for use in datapacks (although I have no idea why you'd want to use datapacks for these).
     */
    public final Holder.Reference<ILoadState> holder;
    LoadStateEnum(){
        this.holder=LoadStateRegistry.INSTANCE.createIntrusiveHolder(this);
    }
    @Override
    public Holder.Reference<ILoadState> getIntrusiveHolder() {
        return holder;
    }
    //?}

    @Nullable
    public static LoadStateEnum fromInt(int i) {
        LoadStateEnum[] vals = values();
        if(i >= 0 && i < vals.length){
            return vals[i];
        }
        return null;
    }

    @Override
    public LoaderPower blockEntityTickingPower() {
        return shouldLoad() ? (permanent() ? LoaderPower.FORCED : LoaderPower.FORCED_MANAGED) : LoaderPower.DISABLED;
    }

    @Override
    public LoaderPower entityForcingPower() {
        return LoaderPower.DISABLED;
    }

    public boolean shouldLoad(){
        return true;
    }

    public boolean permanent(){
        return false;
    }

    @Override
    public boolean overrides(ILoadState state) {
        if(getSuperiorLoadState(state) == this){
            return true;
        }
        return ILoadState.super.overrides(state);
    }

    @Override
    public ILoadState getSuperiorLoadState(ILoadState loadState) {
        if(loadState instanceof LoadStateEnum) {
            if (this.ordinal() > ((LoadStateEnum)loadState).ordinal()) {
                return this;
            } else {
                return loadState;
            }
        }
        return ILoadState.super.getSuperiorLoadState(loadState);
    }

    public ResourceLocation getResourceLocation(){
        return ModResource.of(name().toLowerCase());
    }

    @Override
    public int id() {
        return ordinal();
    }
}
