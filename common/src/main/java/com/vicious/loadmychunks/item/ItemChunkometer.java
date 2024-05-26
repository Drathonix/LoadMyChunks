package com.vicious.loadmychunks.item;

import com.mojang.authlib.GameProfile;
import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.UUID;

public class ItemChunkometer extends ItemHasTooltip {
    public ItemChunkometer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel) level;
            ChunkPos pos = new ChunkPos(player.blockPosition());
            ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(sl, pos);
            if (!LMCConfig.instance.lagometerNeedsChunkOwnership || player.hasPermissions(2) || cdm.containsOwnedLoader(player.getUUID())) {
                MutableComponent response = new TranslatableComponent("loadmychunks.chunkinfo.line1", pos.x, pos.z).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withBold(true));
                player.sendMessage(response,player.getUUID());
                response = new TextComponent("").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(false));
                if (cdm.onCooldown()) {
                    response.append(new TranslatableComponent("loadmychunks.chunkinfo.line2.overticked"));
                } else {
                    if (cdm.getLoadState().shouldLoad()) {
                        response.append(new TranslatableComponent("loadmychunks.chunkinfo.line2.forced"));
                    } else {
                        response.append(new TranslatableComponent("loadmychunks.chunkinfo.line2.notforced"));
                    }
                }
                response.append("\n");
                response.append(new TranslatableComponent("loadmychunks.chunkinfo.line3", cdm.getTickTimer().getDuration())).append("\n");
                StringBuilder csl = new StringBuilder();
                Iterator<UUID> iterator = cdm.getOwners().iterator();
                while (iterator.hasNext()) {
                    UUID u = iterator.next();
                    GameProfile profile = sl.getServer().getProfileCache().get(u);
                    if (profile != null) {
                        csl.append(profile.getName());
                    } else {
                        csl.append(u.toString());
                    }
                    if (iterator.hasNext()) {
                        csl.append(", ");
                    }
                }
                response.append(new TranslatableComponent("loadmychunks.chunkinfo.line4", cdm.getLoaders().size(), csl.toString()));
                if (cdm.onCooldown()) {
                    response.append("\n").append(new TranslatableComponent("loadmychunks.chunkinfo.line5", cdm.getDisabledPeriod().getTimeRemaining()/1000));
                }
                player.sendMessage(response,player.getUUID());
            }
            else{
                player.sendMessage(new TranslatableComponent("loadmychunks.chunkinfo.need_ownership").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)),player.getUUID());
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}
