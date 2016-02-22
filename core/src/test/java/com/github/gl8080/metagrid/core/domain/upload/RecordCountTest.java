package com.github.gl8080.metagrid.core.domain.upload;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class RecordCountTest {

    private RecordCount recordCount;
    
    @Before
    public void setup() {
        recordCount = new RecordCount(10);
    }

    @Test
    public void 生成直後の各フィールドの状態() {
        // verify
        assertThat(recordCount.getTotalCount()).as("全件数").isEqualTo(10);
        assertThat(recordCount.getProcessedCount()).as("処理済み件数").isEqualTo(0);
    }

    @Test
    public void 処理済み件数を加算できる() throws Exception {
        // exercise
        recordCount.increment();
        recordCount.increment();
        recordCount.increment();
        
        // verify
        assertThat(recordCount.getProcessedCount()).isEqualTo(3);
    }
}
