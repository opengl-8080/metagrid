package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.List;
import java.util.Objects;

import com.github.gl8080.metagrid.core.infrastructure.task.Task;

public class CsvFileUploadTask implements Task {
    private CsvUploadFile csv;
    private CsvRecordProcessor<List<String>> processor;
    
    public CsvFileUploadTask(CsvUploadFile csv, CsvRecordProcessor<List<String>> processor) {
        Objects.requireNonNull(csv);
        Objects.requireNonNull(processor);
        
        this.csv = csv;
        this.processor = processor;
    }

    @Override
    public void run() {
        this.csv.each(this.processor);
    }
}
