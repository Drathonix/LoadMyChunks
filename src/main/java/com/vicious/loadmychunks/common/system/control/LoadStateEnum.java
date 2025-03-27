package com.vicious.loadmychunks.common.system.control;

import org.jetbrains.annotations.Nullable;

public enum LoadStateEnum implements LoadStates.ILoadState {
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
        public LoadStates.Power entityForcingPower() {
            return LoadStates.Power.FORCED;
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
    public LoadStates.Power blockEntityTickingPower() {
        return shouldLoad() ? (permanent() ? LoadStates.Power.FORCED : LoadStates.Power.FORCED_MANAGED) : LoadStates.Power.DISABLED;
    }

    @Override
    public LoadStates.Power entityForcingPower() {
        return LoadStates.Power.DISABLED;
    }

    public boolean shouldLoad(){
        return true;
    }

    public boolean permanent(){
        return false;
    }

    @Override
    public boolean overrides(LoadStates.ILoadState state) {
        if(getSuperiorLoadState(state) == this){
            return true;
        }
        return LoadStates.ILoadState.super.overrides(state);
    }

    @Override
    public LoadStates.ILoadState getSuperiorLoadState(LoadStates.ILoadState loadState) {
        if(loadState instanceof LoadStateEnum) {
            if (this.ordinal() > ((LoadStateEnum)loadState).ordinal()) {
                return this;
            } else {
                return loadState;
            }
        }
        return LoadStates.ILoadState.super.getSuperiorLoadState(loadState);
    }

    @Override
    public int id() {
        return ordinal();
    }
}
