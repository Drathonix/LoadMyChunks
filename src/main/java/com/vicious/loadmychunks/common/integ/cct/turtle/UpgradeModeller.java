//? if cct {
package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.mojang.math.Transformation;
import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.impl.client.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
//? if >=1.20.6
/*import net.minecraft.core.component.DataComponentPatch;*/
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class UpgradeModeller<T extends ITurtleUpgrade> implements TurtleUpgradeModeller<T> {
    private static final Transformation leftTransform = getMatrixFor(-0.45f);
    private static final Transformation rightTransform = getMatrixFor(-0.05f);

    private static Transformation getMatrixFor(float offset) {
        return new Transformation(new Vector3f(0.5f + offset, 0.25f, 0.225f),
                null, new Vector3f(0.5f, 0.5f, 0.5f),
                null);
    }
    //? if <=1.20.1 {
    @Override
    public TransformedModel getModel(T t, @Nullable ITurtleAccess iTurtleAccess, TurtleSide side) {
        ItemStack stack = t.getUpgradeItem(null);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        if (stack.hasFoil()) model = ClientPlatformHelper.get().createdFoiledModel(model);
        TransformedModel out = new TransformedModel(model, side == TurtleSide.LEFT ? leftTransform : rightTransform);
        return out;
    }
    //?}


    //? if <1.20.6 && >=1.20.4 {
    /*@Override
    public TransformedModel getModel(T t, @Nullable ITurtleAccess turtle, TurtleSide side, CompoundTag compoundTag) {
        ItemStack stack = t.getUpgradeItem(compoundTag);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        if (stack.hasFoil()) model = ClientPlatformHelper.get().createdFoiledModel(model);
        TransformedModel out = new TransformedModel(model, side == TurtleSide.LEFT ? leftTransform : rightTransform);
        return out;
    }

    *///?}

    //? if >=1.20.6 {
    /*@Override
    public TransformedModel getModel(ITurtleUpgrade upgrade, @Nullable ITurtleAccess turtle, TurtleSide side, DataComponentPatch data) {
        ItemStack stack = upgrade.getUpgradeItem(data);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        if (stack.hasFoil()) model = ClientPlatformHelper.get().createdFoiledModel(model);
        TransformedModel out = new TransformedModel(model, side == TurtleSide.LEFT ? leftTransform : rightTransform);
        return out;
    }
    *///?}
}
//?}
