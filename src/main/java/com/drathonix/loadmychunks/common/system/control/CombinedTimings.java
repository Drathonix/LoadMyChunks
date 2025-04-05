package com.drathonix.loadmychunks.common.system.control;

import com.drathonix.loadmychunks.common.config.LMCConfig;

/**
 * Maintains both the block entity and entity tick timings.
 * @since 1.2.0
 * @author Jack Andersen
 */
public class CombinedTimings {
    private final Timings blockEntities = new Timings();
    private final Timings entities = new Timings();

    /**
     * Gets the tick duration.
     * @return the combined tick time of entities and block entities.
     */
    public long getDuration(){
        return blockEntities.getDuration()+entities.getDuration();
    }

    /**
     * Starts timing block entity ticking.
     */
    public void startBlockEntities(){
        blockEntities.start();
    }

    /**
     * Stops timing block entity ticking.
     */
    public void endBlockEntities(){
        blockEntities.end();
    }

    /**
     * Starts timing entity ticking.
     */
    public void startEntities(){
        entities.start();
    }

    /**
     * Stops timing entity ticking.
     */
    public void endEntities(){
        entities.end();
    }

    /**
     * Checks if the tick duration has exceeded a time limit.
     * @param time the time limit.
     * @return whether the duration is greater than time.
     */
    public boolean durationExceeds(long time){
        return getDuration() > time;
    }

    /**
     * Gets the lag fraction for the timer.
     * @return int where i <= 1.0 and i >= 0 where i = duration/{@link LMCConfig#msPerChunk}
     */
    public float getLagFraction() {
        return (float)Math.max(0,Math.min(1.0, (double) getDuration()/(double) LMCConfig.msPerChunk));
    }
}
