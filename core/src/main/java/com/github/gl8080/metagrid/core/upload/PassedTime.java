package com.github.gl8080.metagrid.core.upload;

import com.github.gl8080.metagrid.core.util.StopWatch;

public class PassedTime {
    
    private long time;
    private StopWatch stopWatch = new StopWatch();
    private boolean isBegan;

    public PassedTime() {}
    
    public PassedTime(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("経過時間に負数は指定できません。");
        }
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public void begin() {
        if (this.isBegan) {
            throw new IllegalStateException("計測がすでに開始しています。");
        }
        
        this.stopWatch.start();
        this.isBegan = true;
    }

    public void end() {
        if (!this.isBegan) {
            throw new IllegalStateException("計測が開始されていません。");
        }
        
        this.stopWatch.stop();
        this.time += this.stopWatch.getTime();
        this.isBegan = false;
        this.stopWatch.clear();
    }
}
