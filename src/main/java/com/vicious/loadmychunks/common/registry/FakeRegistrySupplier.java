package com.vicious.loadmychunks.common.registry;

//? if >1.18.1
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
//? if <=1.16.5
/*import me.shedaniel.architectury.registry.RegistrySupplier;*/

import java.util.function.Supplier;

//? if >1.16.5 {
import com.mojang.datafixers.util.Either;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.Registrar;
//? if <1.19.3
/*import dev.architectury.registry.registries.Registries;*/
//? if >1.19.3
import dev.architectury.registry.registries.RegistrarManager;
//?}
//? if >1.20.3 {
/*import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
*///?}

//Used to instantiate registry suppliers in older versions. We can extend it perfectly fine, thanks.
@SuppressWarnings("NonExtendableApiUsage")
public class FakeRegistrySupplier<T> implements RegistrySupplier<T> {
    private final Supplier<T> supplier;
    private T t;

    public FakeRegistrySupplier(T t) {
        this.supplier = () -> t;
    }
    public FakeRegistrySupplier(Supplier<T> supplier) {
        this.supplier=supplier;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public T get() {
        if(t==null) {
            t=supplier.get();
        }
        return t;
    }

    //? if >1.19.3 {
    @Override
    public RegistrarManager getRegistrarManager() {
        return null;
    }
    //?}

    //? if <1.19.3 && >1.16.5 {
    /*@Override
    public Registries getRegistries() {
        return null;
    }
    *///?}

    //? if >1.16.5 {
    @Override
    public Registrar<T> getRegistrar() {
        return null;
    }
    //?}

    //? if >1.20.3 {
    /*@Override
    public T value() {
        return null;
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return false;
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return false;
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return false;
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        return false;
    }

    //? if >1.20.5 {
    /^@Override
    public boolean is(Holder<T> holder) {
        return false;
    }
    ^///?}

    @Override
    public Stream<TagKey<T>> tags() {
        return Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return null;
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.empty();
    }

    @Override
    public Kind kind() {
        return null;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        return false;
    }
    *///?}
}
