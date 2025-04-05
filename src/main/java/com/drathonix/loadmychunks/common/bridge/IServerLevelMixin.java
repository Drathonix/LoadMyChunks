package com.drathonix.loadmychunks.common.bridge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.entity.PersistentEntitySectionManager;

/**
 * Accessor interface for {@link com.drathonix.loadmychunks.common.mixin.MixinServerLevel}
 */
public interface IServerLevelMixin {
    //PersistentEntitySectionManager<Entity> lmc$getEntityManager();

    boolean lmc$shouldDiscardEntity(Entity entity);

    /*static PersistentEntitySectionManager<Entity> getEntitySectionManager(ServerLevel level) {
        if(level instanceof IServerLevelMixin mixin){
            return mixin.lmc$getEntityManager();
        }
        throw new IllegalStateException("IServerLevelMixin not applied to ServerLevel!");
    }*/
}
