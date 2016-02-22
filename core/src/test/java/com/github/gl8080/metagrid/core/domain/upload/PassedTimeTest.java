package com.github.gl8080.metagrid.core.domain.upload;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.gl8080.metagrid.core.util.StopWatch;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class PassedTimeTest {
    
    @Rule
    public ExpectedException ex = ExpectedException.none();
    
    @Mocked
    private StopWatch stopWatch;
    
    private PassedTime passedTime;
    
    @Before
    public void setup() {
        passedTime = new PassedTime();
    }
    
    @Test
    public void 初期経過時間を指定してインスタンス化できる() {
        // exercise
        passedTime = new PassedTime(123L);
        
        // verify
        assertThat(passedTime.getTime()).as("経過時間").isEqualTo(123L);
    }
    
    @Test
    public void 初期経過時間にマイナスは指定できない() {
        // verify
        ex.expect(IllegalArgumentException.class);
        
        // exercise
        passedTime = new PassedTime(-1L);
    }
    
    @Test
    public void インスタンス生成直後() {
        assertThat(passedTime.getTime()).as("経過時間").isEqualTo(0L);
    }
    
    @Test
    public void 経過時間を記録できる() throws Exception {
        // setup
        new NonStrictExpectations() {{
            stopWatch.getTime(); returns(100L, 200L, 50L);
        }};
        
        // exercise
        passedTime.begin();
        passedTime.end();

        passedTime.begin();
        passedTime.end();

        passedTime.begin();
        passedTime.end();
        
        // verify
        assertThat(passedTime.getTime()).as("経過時間").isEqualTo(350L);
    }
    
    @Test
    public void 記録の開始を二重で実行したらエラー() throws Exception {
        // verify
        ex.expect(IllegalStateException.class);
        
        // exercise
        passedTime.begin();
        passedTime.begin();
    }
    
    @Test
    public void 記録が開始していない状態で終了を実行したらエラー() throws Exception {
        // verify
        ex.expect(IllegalStateException.class);
        
        // exercise
        passedTime.end();
    }

}
