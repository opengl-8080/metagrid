package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.List;

import org.junit.Test;

import mockit.Mocked;
import mockit.Verifications;

public class CsvFileUploadTaskTest {
    
    @Mocked
    private CsvRecordProcessor<List<String>> processor;
    @Mocked
    private CsvUploadFile csv;
    
    @Test
    public void csvファイルの繰り返しメソッドにプロセッサが渡されること() throws Exception {
        // setup
        CsvFileUploadTask task = new CsvFileUploadTask(csv, processor);
        
        // exercise
        task.run();
        
        // verify
        new Verifications() {{
            csv.each(processor); times = 1;
        }};
    }
}
