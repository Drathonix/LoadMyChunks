package com.drathonix.loadmychunks.common.system.control;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import net.minecraft.nbt.CompoundTag;

public class CombinedTimings {
    private final Timings blockEntities = new Timings();
    private final Timings entities = new Timings();

    public long getDuration(){
        return blockEntities.getDuration()+entities.getDuration();
    }

    public void startBlockEntities(){
        blockEntities.start();
    }

    public void endBlockEntities(){
        blockEntities.end();
    }

    public void startEntities(){
        entities.start();
    }

    public void endEntities(){
        entities.end();
    }

    public boolean durationExceeds(long time){
        return getDuration() > time;
    }

    public float getLagFraction() {
        return (float)Math.max(0,Math.min(1.0, (double) getDuration()/(double) LMCConfig.msPerChunk));
    }
}
