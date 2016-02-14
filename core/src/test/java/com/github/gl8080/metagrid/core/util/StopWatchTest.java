package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class StopWatchTest {

    @Mocked
    private Now now;
    
    private StopWatch stopWatch;
    
    @Before
    public void setup() {
        new NonStrictExpectations() {{
            Now.millis(); returns(1000L, 1520L);
        }};
        
        stopWatch = new StopWatch();
    }
    
    @Test
    public void startからstopまでのミリ秒の差を取得できる() {
        // exercise
        stopWatch.start();
        stopWatch.stop();
        
        // verify
        long time = stopWatch.getTime();
        
        assertThat(time).isEqualTo(520L);
    }
    
    @Test
    public void clearで記録を消去できる() {
        // setup
        stopWatch.start();
        stopWatch.stop();
        
        // exercise
        stopWatch.clear();
        
        // verify
        long time = stopWatch.getTime();
        
        assertThat(time).isEqualTo(0L);
    }
}
