package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.mojang.math.Transformation;
import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.impl.client.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class UpgradeModeller<T extends ITurtleUpgrade> implements TurtleUpgradeModeller<T> {
    private static final Transformation leftTransform = getMatrixFor(-0.45f);
    private static final Transformation rightTransform = getMatrixFor(-0.05f);

    private static Transformation getMatrixFor(float offset) {
        return new Transformation(new Vector3f(0.5f+offset,0.25f,0.225f),
                null,new Vector3f(0.5f,0.5f,0.5f),
                null);
    }

    @Override
    public TransformedModel getModel(ITurtleUpgrade upgrade, @Nullable ITurtleAccess turtle, TurtleSide side, DataComponentPatch data) {
        var stack = upgrade.getUpgradeItem(data);
        var model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        if (stack.hasFoil()) model = ClientPlatformHelper.get().createdFoiledModel(model);
        TransformedModel out = new TransformedModel(model, side == TurtleSide.LEFT ? leftTransform : rightTransform);
        return out;
    }

}
