package com.vicious.loadmychunks.common.bridge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

public interface IServerLevelMixin {
    PersistentEntitySectionManager<Entity> lmc$getEntityManager();

    static PersistentEntitySectionManager<Entity> getEntitySectionManager(ServerLevel level) {
        if(level instanceof IServerLevelMixin mixin){
            return mixin.lmc$getEntityManager();
        }
        throw new IllegalStateException("IServerLevelMixin not applied to ServerLevel!");
    }
}
