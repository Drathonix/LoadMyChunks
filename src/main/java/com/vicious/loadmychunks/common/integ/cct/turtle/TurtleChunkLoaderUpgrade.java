package com.vicious.loadmychunks.common.integ.cct.turtle;

//? if !cct {
/*public class TurtleChunkLoaderUpgrade implements ITurtleUpgrade {
}
*///?}

//? if cct {
import com.vicious.loadmychunks.common.integ.cct.CCTRegistryContent;
import com.vicious.loadmychunks.common.registry.LMCContent;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.upgrades.UpgradeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

public class TurtleChunkLoaderUpgrade implements ITurtleUpgrade {
    public TurtleChunkLoaderUpgrade(){}

    @Override
    public UpgradeType<? extends ITurtleUpgrade> getType() {
        return CCTRegistryContent.type.get();
    }

    @Override
    public Component getAdjective() {
        return Component.translatable("loadmychunks.turtle.adjective.loading");
    }

    @Override
    public ItemStack getCraftingItem() {
        return LMCContent.chunkLoaderBlock.get().asItem().getDefaultInstance();
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TurtleChunkLoaderPeripheral(turtle, side);
    }

    @Override
    public TurtleUpgradeType getUpgradeType() {
        return TurtleUpgradeType.PERIPHERAL;
    }
}
//?}
