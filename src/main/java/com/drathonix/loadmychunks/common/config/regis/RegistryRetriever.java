package com.drathonix.loadmychunks.common.config.regis;

import com.drathonix.loadmychunks.common.util.ModResource;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class RegistryRetriever<T> {
    protected ResourceLocation location;
    private T object;

    public RegistryRetriever(String key){
        this(ModResource.parse(key.replaceFirst("\\.",":")));
    }

    public RegistryRetriever(ResourceLocation location) {
        this.location=location;
    }

    abstract T retrieve();

    public String serializable() {
        return location.toString().replaceFirst(":",".");
    }

    public T get() {
        if(object == null){
            object = retrieve();
        }
        return object;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RegistryRetriever<?> that = (RegistryRetriever<?>) object;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }
}
