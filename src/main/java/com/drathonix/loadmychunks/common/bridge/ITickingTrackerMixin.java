package com.drathonix.loadmychunks.common.bridge;

/**
 * Accessor interface for {@link com.drathonix.loadmychunks.common.mixin.MixinTickingTracker}
 * @since 1.2.0
 * @author Jack Andersen
 */
public interface ITickingTrackerMixin {
    /**
     * Checks for a ticket of type {@link com.drathonix.loadmychunks.common.system.control.ChunkForcer#ENTITY}
     * @param chunkPos the target pos.
     * @return whether the ticket type is present.
     */
    boolean lmc$hasEntityForcingTicket(long chunkPos);

    /**
     * Checks for a ticket of type {@link com.drathonix.loadmychunks.common.system.control.ChunkForcer#ENTITY}
     * @param chunkPos the target pos.
     * @param tracker some tracker.
     * @return whether the ticket type is present.
     */
    static boolean hasEntityForcingTicket(Object tracker, long chunkPos){
        if(tracker instanceof ITickingTrackerMixin){
            return ((ITickingTrackerMixin)tracker).lmc$hasEntityForcingTicket(chunkPos);
        }
        else{
            throw new IllegalStateException("Distance manager mixin was not applied!");
        }
    }
}
