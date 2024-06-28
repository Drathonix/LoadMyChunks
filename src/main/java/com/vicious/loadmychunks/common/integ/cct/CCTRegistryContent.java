//? if cct {
package com.vicious.loadmychunks.common.integ.cct;

import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoader;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.integ.cct.turtle.UpgradeModeller;
import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.LoaderTypeRegistry;
//? if >=1.20.6
import dan200.computercraft.api.upgrades.UpgradeType;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
//? if >1.16.5
import dev.architectury.registry.registries.RegistrySupplier;

//? if >1.16.5 && <1.20.4
/*import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;*/
//? if >1.16.5 && <1.20.6
/*import dan200.computercraft.api.upgrades.UpgradeSerialiser;*/

//? if <=1.16.5 {
/*import me.shedaniel.architectury.registry.RegistrySupplier;
*///?}

//? if >=1.19.2 {
import dan200.computercraft.client.turtle.TurtleUpgradeModellers;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dev.architectury.registry.registries.RegistrySupplier;
//?}

import java.util.*;

public class CCTRegistryContent {
    //? if >=1.20.6
    public static List<RegistrySupplier<UpgradeType<? extends TurtleChunkLoaderUpgrade>>> registrySuppliers = new ArrayList<>();
    //? if <1.20.6 && >1.20.1
    /*public static List<RegistrySupplier<UpgradeSerialiser<? extends ITurtleUpgrade>>> registrySuppliers = new ArrayList<>();*/
    //? if <=1.20.1 && >1.16.5
    /*public static List<RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>>> registrySuppliers = new ArrayList<>();*/
    //? if <=1.16.5
    /*public static List<RegistrySupplier<ITurtleUpgrade>> registrySuppliers = new ArrayList<>();*/

    public static void registerClient() {
        //? if >1.18.2 {
        registrySuppliers.forEach(v->{
            TurtleUpgradeModellers.register(v.get(), new UpgradeModeller<>());
        });
        //?}
        LoaderTypeRegistry.register(LoaderTypes.CCT_TURTLE_LOADER, TurtleChunkLoader::new);
    }
}
//?}
