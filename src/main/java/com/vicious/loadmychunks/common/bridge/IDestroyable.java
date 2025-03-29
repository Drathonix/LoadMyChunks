package com.vicious.loadmychunks.common.bridge;

/**
 * Interface for block entities, called when the block is broken.
 */
public interface IDestroyable {
    /**
     * Called when the block entity's block is broken.
     */
    void loadMyChunks$destroy();
}
