package com.github.gl8080.metagrid.core.util;

public class StopWatch {
    
    private long startTime;
    private long stopTime;
    
    public void start() {
        this.startTime = Now.millis();
    }

    public void stop() {
        this.stopTime = Now.millis();
    }
    
    public long getTime() {
        return this.stopTime - this.startTime;
    }

    public void clear() {
        this.startTime = 0L;
        this.stopTime = 0L;
    }

    @Override
    public String toString() {
        return "StopWatch [startTime=" + startTime + ", stopTime=" + stopTime + "]";
    }
}
