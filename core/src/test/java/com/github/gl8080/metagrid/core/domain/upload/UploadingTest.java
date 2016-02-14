package com.github.gl8080.metagrid.core.domain.upload;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class UploadingTest {
    
    
    public class 生成直後の状態 {
        
        private Uploading uploading;
        
        @Before
        public void setup() {
            uploading = new Uploading();
        }
        
        @Test
        public void 各フィールドの状態() {
            // verify
            assertThat(uploading.getUploadFile()).as("アップロードファイル").isNull();
            assertThat(uploading.getRecordCount()).as("処理件数").isNull();
            assertThat(uploading.getProcessingTime()).as("処理時間").isNotNull();
            assertThat(uploading.getStatus()).as("状態").isNotNull();
            assertThat(uploading.getErrorFile()).as("エラーファイル").isNotNull();
        }

        @Test
        public void 状態は_待機() {
            // verify
            assertThat(uploading.getStatus()).isSameAs(Status.WAITING);
        }
    }

}
