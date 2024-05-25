package com.vicious.loadmychunks.system.control;

public class Period {
    private final long periodEnd;

    public Period(long periodEnd){
        this.periodEnd=periodEnd;
    }

    public static Period after(long millis) {
        return new Period(System.currentTimeMillis()+millis);
    }

    public boolean hasEnded(){
        return System.currentTimeMillis() >= periodEnd;
    }

    public long getEnd() {
        return periodEnd;
    }

    public long getTimeRemaining() {
        return periodEnd-System.currentTimeMillis();
    }
}
