package com.drathonix.loadmychunks.common.system.upgrades;

/**
 * Items which implement this interface can be used as upgrades in chunk loaders.
 * @since 1.2.0
 */
public interface IChunkLoaderUpgradeProvider {
    /**
     * Controls item can be used as an upgrade.
     * @return whether the upgrade is enabled.
     */
    default boolean enabled(){
        return true;
    }

    /**
     * The maximum number of same upgrades of this item a chunk loader can have.
     * @return the number of upgrades allowed.
     */
    default int maxUpgradesPresent(){
        return 1;
    }
}
