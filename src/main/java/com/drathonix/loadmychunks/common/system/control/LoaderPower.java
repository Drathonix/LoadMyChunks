package com.drathonix.loadmychunks.common.system.control;

/**
 * Controls the power of {@link ILoadState#blockEntityTickingPower()} and {@link ILoadState#entityForcingPower()}
 * @since 1.2.0
 * @author Jack Andersen
 */
public enum LoaderPower {
    /**
     * Disables ticking.
     */
    DISABLED,
    /**
     * Forced ticks will be enabled but can be temporarily disabled if overticking occurs.
     */
    FORCED_MANAGED,
    /**
     * Forced without limits.
     */
    FORCED,
    /**
     * Disables ticking and completely blocks all other sources from enabling it.
     */
    DISABLED_PERMANENT;

    public boolean isManaged() {
        return this == FORCED_MANAGED;
    }
}
