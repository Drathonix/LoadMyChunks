package com.drathonix.loadmychunks.common.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
//? if <1.19.5
/*import net.minecraft.world.level.material.Material;*/

/**
 * Multi-versioning Utility Class for miscellaneous methods.
 */
public class Other {
    /**
     * Creates a default properties object.
     * @return some properties.
     */
    public static @NotNull BlockBehaviour.Properties properties() {
        //? if =1.20.1 && forge {
        /*return BlockBehaviour.Properties.method_9637().requiresCorrectToolForDrops();
        *///?} else if <1.19.5 {
        /*return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops();
        *///?} else if >1.19.4 {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops();
        //?}
    }

    /**
     * Creates a default properties object with the strength and blastResistance provided.
     * @return some properties.
     */
    public static @NotNull BlockBehaviour.Properties properties(float strength, float blastResistance) {
        //? if =1.20.1 && forge {
        /*return BlockBehaviour.Properties.method_9637().requiresCorrectToolForDrops().strength(strength, blastResistance);
        *///?} else if <1.19.5 {
        /*return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(strength, blastResistance);
        *///?} else if >1.19.4 {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(strength, blastResistance);
        //?}
    }
}
