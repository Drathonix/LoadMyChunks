package com.vicious.loadmychunks.common.integ.cct;

//? if !cct {
/*public class CCTInit {

}
*///?}

//? if cct {
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.integ.cct.turtle.UpgradeModeller;
import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.LoaderTypeRegistry;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.upgrades.UpgradeType;
import dan200.computercraft.client.turtle.TurtleUpgradeModellers;
import dev.architectury.registry.registries.RegistrySupplier;

public class CCTRegistryContent {
    public static RegistrySupplier<UpgradeType<TurtleChunkLoaderUpgrade>> type;

    public static void registerClient() {
        TurtleUpgradeModellers.register(type.get(), new UpgradeModeller<>());
        LoaderTypeRegistry.register(LoaderTypes.CCT_TURTLE_LOADER, TurtleChunkLoader::new);
    }
}
//?}
