package com.drathonix.loadmychunks.common.bridge;

/**
 * Accessor Mixin for {@link com.drathonix.loadmychunks.common.mixin.MixinDistanceManager}
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface IDistanceManagerMixin {
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
