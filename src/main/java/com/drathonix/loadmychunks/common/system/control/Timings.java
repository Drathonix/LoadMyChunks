package com.drathonix.loadmychunks.common.system.control;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import net.minecraft.nbt.CompoundTag;

public class Timings {
    private long startSystemMS;
    private long lastDuration;

    public Timings() {
    }

    public long getDuration() {
        return lastDuration;
    }

    public void start() {
        startSystemMS = System.currentTimeMillis();
    }

    public void end() {
        lastDuration = System.currentTimeMillis() - startSystemMS;
    }
}
