//? if neoforge && cct {
/*package com.vicious.loadmychunks.neoforge.integ;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.integ.cct.turtle.TurtleChunkLoaderUpgrade;
import com.vicious.loadmychunks.common.registry.FakeRegistrySupplier;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCTNeo {
    public static DeferredRegister<UpgradeType<? extends ITurtleUpgrade>> turtleUpgrades = DeferredRegister.create(ITurtleUpgrade.typeRegistry(),LoadMyChunks.MOD_ID);
    static {
        CCTRegistryContent.type = new FakeRegistrySupplier<>(turtleUpgrades.register("chunk_loader", ()->UpgradeType.simple(new TurtleChunkLoaderUpgrade())));
    }

    public static void register(IEventBus bus) {
        turtleUpgrades.register(bus);
    }
}
*///?}
