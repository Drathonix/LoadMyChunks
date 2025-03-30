package com.drathonix.loadmychunks.common.system.control;

public enum LoaderPower {
    DISABLED,
    FORCED_MANAGED,
    FORCED,
    DISABLED_PERMANENT;

    public boolean isManaged() {
        return this == FORCED_MANAGED;
    }
}
