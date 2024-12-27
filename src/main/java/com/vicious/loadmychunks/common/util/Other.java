package com.vicious.loadmychunks.common.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
//? if <1.19.5
/*import net.minecraft.world.level.material.Material;*/

public class Other {
    public static BlockBehaviour.Properties properties() {
        //? if =1.20.1 && forge {
        /*return BlockBehaviour.Properties.method_9637().requiresCorrectToolForDrops();
        *///?} else if <1.19.5 {
        /*return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops();
        *///?} else if >1.19.4 {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops();
        //?}
    }

    public static BlockBehaviour.Properties properties(float strength, float blastResistance) {
        //? if =1.20.1 && forge {
        /*return BlockBehaviour.Properties.method_9637().requiresCorrectToolForDrops().strength(strength, blastResistance);
        *///?} else if <1.19.5 {
        /*return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(strength, blastResistance);
        *///?} else if >1.19.4 {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(strength, blastResistance);
        //?}
    }
}
