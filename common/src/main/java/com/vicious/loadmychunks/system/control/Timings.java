package com.vicious.loadmychunks.system.control;

import com.vicious.loadmychunks.config.LMCConfig;
import net.minecraft.nbt.CompoundTag;

public class Timings {
    private long startSystemMS;
    private long endSystemMS;
    private long lastDuration;

    public Timings(){}

    public void load(CompoundTag tag){
        startSystemMS = tag.getLong("start");
        endSystemMS = tag.getLong("end");
    }

    public long getDuration(){
        return lastDuration;
    }

    public void start(){
        startSystemMS = System.currentTimeMillis();
    }

    public void end(){
        endSystemMS = System.currentTimeMillis();
        lastDuration = getDuration();
    }

    public boolean durationExceeds(long time){
        return getDuration() > time;
    }

    public CompoundTag save() {
        CompoundTag out = new CompoundTag();
        out.putLong("start",startSystemMS);
        out.putLong("end",endSystemMS);
        return out;
    }

    public float getLagFraction() {
        return (float)Math.min(1.0, (double) getDuration()/(double) LMCConfig.instance.msPerChunk);
    }

    public long getStart() {
        return startSystemMS;
    }

    public long getEnd() {
        return endSystemMS;
    }
}
