package com.drathonix.loadmychunks.common.registry;

import com.drathonix.loadmychunks.common.util.ModResource;
import net.minecraft.resources.ResourceLocation;

/**
 * Just a storage class for some constant loader type keys.
 * @since 1.0.0
 * @author Jack Andersen
 */
public class LoaderTypeKeys {
    public static final ResourceLocation PLACED_LOADER = ModResource.of("placed");
    public static final ResourceLocation PLACED_EXTENSION_LOADER = ModResource.of("placed_extension");
    public static final ResourceLocation PHANTOM_LOADER = ModResource.of("phantom");
    public static final ResourceLocation CCT_TURTLE_LOADER = ModResource.of("cct_turtle");
}
