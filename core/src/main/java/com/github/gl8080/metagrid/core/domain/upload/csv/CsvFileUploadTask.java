package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.Objects;

import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.infrastructure.task.Task;

public class CsvFileUploadTask implements Task {
    private UploadFile csv;
    private FileLineProcessor processor;
    
    public CsvFileUploadTask(UploadFile csv, FileLineProcessor processor) {
        Objects.requireNonNull(csv);
        Objects.requireNonNull(processor);
        
        this.csv = csv;
        this.processor = processor;
    }

    @Override
    public void run() {
        this.csv.eachLine(this.processor);
    }
}
