package com.drathonix.loadmychunks.common.util;

import com.drathonix.loadmychunks.common.LoadMyChunks;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Convenience class for making resource locations easily.
 */
public class ModResource {
    /**
     * Makes a resource location "modid:string"
     * @param string the key
     * @return a resource location.
     */
    public static @NotNull ResourceLocation of(@NotNull String string){
        //? if <1.20.7
        return new ResourceLocation(LoadMyChunks.MOD_ID,string);
        //? if >1.20.6
        /*return ResourceLocation.fromNamespaceAndPath(LoadMyChunks.MOD_ID,string);*/
    }

    /**
     * Parses a resource location.
     * @param string the resource location string
     * @return a parsed resource location.
     */
    public static @Nullable ResourceLocation parse(@NotNull String string) {
        //? if <1.20.7 {
        try {
            return new ResourceLocation(string);
        } catch(Throwable t){
            return null;
        }
        //?} else {
        /*return ResourceLocation.tryParse(string);
        *///?}
    }
}
