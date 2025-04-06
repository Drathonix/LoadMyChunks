package com.drathonix.loadmychunks.common.config.regis;

import com.drathonix.loadmychunks.common.util.ModResource;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class RegistryRetriever<T> {
    protected ResourceLocation location;
    private T object;

    public RegistryRetriever(String key){
        this(ModResource.parse(convertOldPatch(key)));
    }

    /**
     * In past versions of LMC a different file format was used and I had to keep colons out of strings. This fixes that.
     * @param val original value
     * @return possibly converted key.
     */
    private static String convertOldPatch(String val){
        for (int i = 0; i < val.length(); i++) {
            if(val.charAt(i) == ':'){
                return val;
            }
            else if(val.charAt(i) == '.'){
                return val.replaceFirst("\\.",":");
            }
        }
        return val;
    }


    public RegistryRetriever(ResourceLocation location) {
        this.location=location;
    }

    abstract T retrieve();

    public String serializable() {
        return location.toString();
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
