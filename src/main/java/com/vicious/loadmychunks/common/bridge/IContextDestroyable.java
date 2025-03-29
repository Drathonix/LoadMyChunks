package com.vicious.loadmychunks.common.bridge;

/**
 * Interface for block entities components, called when the block is broken.
 */
public interface IContextDestroyable {
    /**
     * Called once for each block entity component when broken.
     * @param context the source context.
     */
    void loadMyChunks$destroy(Object context);
}
