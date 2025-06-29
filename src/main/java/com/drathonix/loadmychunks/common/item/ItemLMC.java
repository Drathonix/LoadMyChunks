package com.drathonix.loadmychunks.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemLMC extends Item {
    public ItemLMC(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        return useOnCtx(useOnContext);
    }

    protected InteractionResult useOnCtx(UseOnContext context) {
        return InteractionResult.PASS;
    }
}
