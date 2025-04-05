package com.drathonix.loadmychunks.common.config.regis;

import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.system.control.ILoadState;
import com.vicious.persist.mappify.registry.Stringify;
import net.minecraft.resources.ResourceLocation;

public class LoadStateRetriever extends RegistryRetriever<ILoadState> {
    static {
        Stringify.register(LoadStateRetriever.class, LoadStateRetriever::new, LoadStateRetriever::serializable);
    }

    public LoadStateRetriever(String key) {
        super(key);
    }

    public LoadStateRetriever(ResourceLocation key) {
        super(key);
    }

    @Override
    ILoadState retrieve() {
        return LoadStateRegistry.INSTANCE.get(location);
    }
}
