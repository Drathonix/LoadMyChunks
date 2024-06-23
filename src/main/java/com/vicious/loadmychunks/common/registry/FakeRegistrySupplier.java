package com.vicious.loadmychunks.common.registry;

//if <=1.16.5
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;

public class FakeRegistrySupplier<T> implements RegistrySupplier<T> {
    private final T t;

    public FakeRegistrySupplier(T t) {
        this.t=t;
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
        return t;
    }
}
