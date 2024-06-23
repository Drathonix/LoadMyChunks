package com.vicious.loadmychunks.common.registry;

//? if <=1.16.5
/*import me.shedaniel.architectury.registry.RegistrySupplier;*/
//? if >1.16.5 {
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
//?}
import net.minecraft.resources.ResourceLocation;

//Used to instantiate registry suppliers in older versions. We can extend it perfectly fine thanks.
@SuppressWarnings("NonExtendableApiUsage")
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

    @Override
    public RegistrarManager getRegistrarManager() {
        return null;
    }

    @Override
    public Registrar<T> getRegistrar() {
        return null;
    }
}
