package com.vicious.loadmychunks.common.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a queue of runnables to execute on the next tick. Helps to avoid get loops.
 */
public class TickDelayer {
    private static final List<DelayedRunnable> runnables = new ArrayList<>();
    private static long tickNum = 0;

    public static void tick(){
        for (int i = 0; i < runnables.size(); i++) {
            if(runnables.get(i) == null){
                runnables.remove(i);
                i--;
            }
            if(runnables.get(i).targetTick <= tickNum) {
                runnables.remove(i).run();
                i--;
            }
        }
        tickNum++;
    }

    public static void delayOneTick(Runnable runnable){
        runnables.add(new DelayedRunnable(runnable,tickNum+1));
    }

    public static long getTick() {
        return tickNum;
    }

    private static class DelayedRunnable implements Runnable{
        private final Runnable runnable;
        private final long targetTick;

        private DelayedRunnable(Runnable runnable, long targetTick){
            this.runnable = runnable;
            this.targetTick = targetTick;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }
}
