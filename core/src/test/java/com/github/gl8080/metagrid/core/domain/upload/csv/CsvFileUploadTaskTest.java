package com.github.gl8080.metagrid.core.domain.upload.csv;

import org.junit.Test;

import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;

import mockit.Mocked;
import mockit.Verifications;

public class CsvFileUploadTaskTest {
    
    @Mocked
    private FileLineProcessor processor;
    @Mocked
    private UploadFile csv;
    
    @Test
    public void csvファイルの繰り返しメソッドにプロセッサが渡されること() throws Exception {
        // setup
        CsvFileUploadTask task = new CsvFileUploadTask(csv, processor);
        
        // exercise
        task.run();
        
        // verify
        new Verifications() {{
            csv.eachLine(processor); times = 1;
        }};
    }
}
