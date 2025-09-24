package com.drathonix.loadmychunks.common.util;

import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.mojang.authlib.GameProfile;
//? if >1.16.5 {
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
//?}
//? if >1.21.3 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
*///?}
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
//? if <1.19.5
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

/**
 * Multi-versioning Utility Class for miscellaneous methods.
 */
public class MultiversioningHelper {
    /**
     * Creates a default properties object.
     * @return some properties.
     */
    public static @NotNull BlockBehaviour.Properties properties(String key) {
        //? if >1.21.3 {
        /*return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().setId(ResourceKey.create(Registries.BLOCK,ModResource.of(key)));
        *///?} else if >1.19.4 {
        /*return BlockBehaviour.Properties.of().requiresCorrectToolForDrops();
        *///?} else {
        return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops();
        //?}
    }

    /**
     * Creates a default properties object with the strength and blastResistance provided.
     * @return some properties.
     */
    public static @NotNull BlockBehaviour.Properties properties(String key, float strength, float blastResistance) {
        return properties(key).strength(strength,blastResistance);
    }

    public static void serverLevel(BlockEntity blockEntity, Consumer<ServerLevel> cons) {
        serverLevel(blockEntity.getLevel(), cons);
    }

    public static void serverLevel(Level level, Consumer<ServerLevel> cons) {
        if(level instanceof ServerLevel){
            cons.accept((ServerLevel) level);
        }
    }

    public static boolean isRemoved(Entity entity) {
        //? if >1.16.5 {
        return entity.isRemoved();
        //?} else {
        /*return entity.removed;
        *///?}
    }

    public static ChunkPos chunkPosOf(Entity entity) {
        //? if >1.16.5 {
        return entity.chunkPosition();
        //?} else {
        /*return new ChunkPos(entity.xChunk, entity.zChunk);
        *///?}
    }

    public static void serverLevel(Entity arg, Consumer<ServerLevel> cons) {
        //? if >1.19.4 {
        /*Level l = arg.level();
        *///?} else {
        Level l = arg.level;
        //?}
        serverLevel(l, cons);
    }

    /**
     * Checks if the object is an optional and if not puts it in an optional. The expected output must not be of type {@literal Optional<Optional<?>>}
     * @param obj the possible optional.
     * @return an optional.
     * @param <T> the optional value type.
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> enforceOptional(@Nullable Object obj) {
        if(obj instanceof Optional){
            return (Optional<T>) obj;
        }
        else{
            return (Optional<T>) Optional.ofNullable(obj);
        }
    }

    public static <T> @Nullable T serverLevel(BlockEntity blockEntity, Function<ServerLevel,T> func) {
        Level l = blockEntity.getLevel();
        if(l instanceof ServerLevel){
            return func.apply((ServerLevel) l);
        }
        return null;
    }

    public static <T> T enforceValue(Object obj) {
        if(obj instanceof Optional<?>){
            return enforceValue((T)((Optional<?>) obj).get());
        }
        //? if >1.16.5 {
        else if(obj instanceof Holder.Reference<?>){
            return (T)((Holder.Reference<?>) obj).value();
        }
        //?}
        else{
            return (T)obj;
        }
    }
}
