package com.drathonix.loadmychunks.common.item;

import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import com.mojang.authlib.GameProfile;
import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import com.drathonix.loadmychunks.common.util.Message;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

public class ItemChunkometer extends ItemHasTooltip {
    public ItemChunkometer(Properties properties) {
        super(properties,1);
    }

    @Override
    protected InteractionResult useOnCtx(UseOnContext useOnContext) {
        MultiversioningHelper.serverLevel(useOnContext.getLevel(),sl->{
            ServerPlayer player = (ServerPlayer) useOnContext.getPlayer();
            if(player == null){
                return;
            }
            ChunkPos pos = new ChunkPos(player.blockPosition());
            ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData(sl, pos);
            if (!LMCConfig.lagometerNeedsChunkOwnership || player.hasPermissions(2) || cdm.containsOwnedLoader(player.getUUID())) {
                MutableComponent response = Message.styled(Message.translatable("loadmychunks.chunkinfo.line1", pos.x, pos.z),ChatFormatting.WHITE,true,false);
                Message.send(player,response);
                response = Message.styled(Message.empty(),ChatFormatting.AQUA,false,false);
                if (cdm.onCooldown()) {
                    response = Message.append(response,Message.translatable("loadmychunks.chunkinfo.line2.overticked"));
                } else {
                    if (cdm.getLoadState().shouldLoad()) {
                        if(cdm.getLoadState().shouldForceEntities()) {
                            response = Message.append(response,Message.translatable("loadmychunks.chunkinfo.line2.forced.entity_ticking"));
                        } else {
                            response = Message.append(response,Message.translatable("loadmychunks.chunkinfo.line2.forced"));
                        }
                    } else {
                        response = Message.append(response,Message.translatable("loadmychunks.chunkinfo.line2.notforced"));
                    }
                }
                response = Message.append(response,"\n");
                response = Message.append(response,Message.translatable("loadmychunks.chunkinfo.line3",cdm.getTickTimer().getDuration()));
                StringBuilder csl = new StringBuilder();
                Iterator<UUID> iterator = cdm.getPlayerOwners().iterator();
                while (iterator.hasNext()) {
                    UUID u = iterator.next();
                    Optional<GameProfile> profile = MultiversioningHelper.enforceOptional(sl.getServer().getProfileCache().get(u));
                    if (profile.isPresent()) {
                        csl.append(profile.get().getName());
                    } else {
                        csl.append(u.toString());
                    }
                    if (iterator.hasNext()) {
                        csl.append(", ");
                    }
                }
                if(!cdm.getLoaders().isEmpty()) {
                    response = Message.append(response,"\n");
                    response = Message.append(response, Message.translatable("loadmychunks.chunkinfo.line4", cdm.getLoaders().size(), csl.toString()));
                    if (cdm.onCooldown()) {
                        response = Message.append(response, "\n");
                        response = Message.append(response, Message.translatable("loadmychunks.chunkinfo.line5", cdm.getDisabledPeriod().getTimeRemaining() / 1000));
                    }
                }
                Message.send(player,response);
            }
            else{
                Message.send(player,Message.styled(Message.translatable("loadmychunks.chunkinfo.need_ownership"),ChatFormatting.RED,false,false));
            }
        });
        return InteractionResult.SUCCESS;
    }
}

