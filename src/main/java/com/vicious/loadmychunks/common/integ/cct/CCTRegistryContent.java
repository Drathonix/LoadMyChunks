//? if cct {
package com.vicious.loadmychunks.common.integ.cct;

import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.integ.cct.turtle.UpgradeModeller;
import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.LoaderTypeRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
//? if >=1.20.6
/*import dan200.computercraft.api.upgrades.UpgradeType;*/
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import dan200.computercraft.api.upgrades.UpgradeSerialiser;
import dan200.computercraft.client.turtle.TurtleUpgradeModellers;
import dev.architectury.registry.registries.RegistrySupplier;

import java.util.*;

public class CCTRegistryContent {
    //? if >=1.20.6
    /*public static List<RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>>> registrySuppliers = new ArrayList<>();*/
    //? if <1.20.6 && >1.20.1
    /*public static List<RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>>> registrySuppliers = new ArrayList<>();*/
    //? if <=1.20.1
    public static List<RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>>> registrySuppliers = new ArrayList<>();

    public static void registerClient() {
        registrySuppliers.forEach(v->{
            TurtleUpgradeModellers.register(v.get(), new UpgradeModeller<>());
        });
        LoaderTypeRegistry.register(LoaderTypes.CCT_TURTLE_LOADER, TurtleChunkLoader::new);
    }
}
//?}
