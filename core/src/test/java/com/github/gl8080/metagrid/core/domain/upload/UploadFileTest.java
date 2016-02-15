package com.github.gl8080.metagrid.core.domain.upload;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class UploadFileTest {
    
    
    public class 生成直後の状態 {
        
        private UploadFile uploadFile;
        
        @Before
        public void setup() {
            uploadFile = new UploadFile("fileName", new File("test.txt"));
        }
        
        @Test
        public void 各フィールドの状態() {
            // verify
            assertThat(uploadFile.getRecordCount()).as("処理件数").isNull();
            assertThat(uploadFile.getProcessingTime()).as("処理時間").isNotNull();
            assertThat(uploadFile.getStatus()).as("状態").isNotNull();
            assertThat(uploadFile.getErrorFile()).as("エラーファイル").isNotNull();
        }

        @Test
        public void 状態は_待機() {
            // verify
            assertThat(uploadFile.getStatus()).isSameAs(Status.WAITING);
        }
    }

}
