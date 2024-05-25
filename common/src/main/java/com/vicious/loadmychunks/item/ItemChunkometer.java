package com.vicious.loadmychunks.item;

import com.mojang.authlib.GameProfile;
import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.system.ChunkDataModule;
import com.vicious.loadmychunks.system.ChunkDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

public class ItemChunkometer extends ItemHasTooltip {
    public ItemChunkometer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level instanceof ServerLevel sl) {
            ChunkPos pos = new ChunkPos(player.blockPosition());
            ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(sl, pos);
            if (!LMCConfig.instance.lagometerNeedsChunkOwnership || player.hasPermissions(2) || cdm.containsOwnedLoader(player.getUUID())) {
                MutableComponent response = Component.translatable("loadmychunks.chunkinfo.line1", pos.x, pos.z).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withBold(true)).append("\n");
                if (cdm.onCooldown()) {
                    response.append(Component.translatable("loadmychunks.chunkinfo.line2.overticked"));
                } else {
                    if (cdm.getLoadState().shouldLoad()) {
                        response.append(Component.translatable("loadmychunks.chunkinfo.line2.forced"));
                    } else {
                        response.append(Component.translatable("loadmychunks.chunkinfo.line2.notforced"));
                    }
                }
                response.append("\n");
                response.append(Component.translatable("loadmychunks.chunkinfo.line3", cdm.getTickTimer().getDuration())).append("\n");
                StringBuilder csl = new StringBuilder();
                Iterator<UUID> iterator = cdm.getOwners().iterator();
                while (iterator.hasNext()) {
                    UUID u = iterator.next();
                    Optional<GameProfile> profile = sl.getServer().getProfileCache().get(u);
                    if (profile.isPresent()) {
                        csl.append(profile.get().getName());
                    } else {
                        csl.append(u.toString());
                    }
                    if (iterator.hasNext()) {
                        csl.append(", ");
                    }
                }
                response.append(Component.translatable("loadmychunks.chunkinfo.line4", cdm.getLoaders().size(), csl.toString()));
                if (cdm.onCooldown()) {
                    response.append("\n").append(Component.translatable("loadmychunks.chunkinfo.line5", cdm.getDisabledPeriod().getTimeRemaining()/1000));
                }
                player.sendSystemMessage(response);
            }
            else{
                player.sendSystemMessage(Component.translatable("loadmychunks.chunkinfo.need_ownership").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}
