package com.drathonix.loadmychunks.common.bridge;

import com.drathonix.loadmychunks.common.integ.c2me.bridge.IC2METickingTracker;
import net.minecraft.server.level.DistanceManager;

/**
 * Accessor Mixin for {@link com.drathonix.loadmychunks.common.mixin.MixinDistanceManager}
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface IDistanceManagerMixin {
    static void lmc$setC2MENTS(Object manager, IC2METickingTracker tracker) {
        if(manager instanceof IDistanceManagerMixin){
            ((IDistanceManagerMixin) manager).lmc$setC2MENTS(tracker);
        }
        else{
            throw new IllegalStateException("Distance manager mixin was not applied!");
        }
    }
    void lmc$setC2MENTS(IC2METickingTracker tracker);

    /**
     * Checks for a ticket of type {@link com.drathonix.loadmychunks.common.system.control.ChunkForcer#ENTITY}
     * @param chunkPos the target pos.
     * @return whether the ticket type is present.
     */
    boolean lmc$hasEntityForcingTicket(long chunkPos);

    /**
     * Checks for a ticket of type {@link com.drathonix.loadmychunks.common.system.control.ChunkForcer#ENTITY}
     * @param chunkPos the target pos.
     * @param manager some distance manager.
     * @return whether the ticket type is present.
     */
    static boolean lmc$hasEntityForcingTicket(Object manager, long chunkPos){
        if(manager instanceof IDistanceManagerMixin){
            return ((IDistanceManagerMixin)manager).lmc$hasEntityForcingTicket(chunkPos);
        }
        else{
            throw new IllegalStateException("Distance manager mixin was not applied!");
        }
    }

    boolean lmc$inEntityTickingRange(long pos);
    boolean lmc$hasPlayersNearby(long pos);
}
