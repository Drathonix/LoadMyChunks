package com.vicious.loadmychunks.common.system.control;

import com.vicious.loadmychunks.common.util.ModResource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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

        @Override
        public LoaderPower entityForcingPower() {
            return LoaderPower.FORCED;
        }
    },
    PERMANENTLY_DISABLED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    };

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
