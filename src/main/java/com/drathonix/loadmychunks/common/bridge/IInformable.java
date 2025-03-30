package com.drathonix.loadmychunks.common.bridge;

/**
 * Interface that indicates an object can receive chunk lag stats.
 */
public interface IInformable {
    /**
     * Called once per chunk lag update for each informable in the chunk.
     * @param frac the chunk lag length divided by {@link com.drathonix.loadmychunks.common.config.LMCConfig#msPerChunk}
     */
    void informLagFrac(float frac);
}
