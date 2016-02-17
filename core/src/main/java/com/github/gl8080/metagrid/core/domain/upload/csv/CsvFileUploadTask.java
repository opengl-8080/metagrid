package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.List;
import java.util.Objects;

import com.github.gl8080.metagrid.core.infrastructure.task.Task;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class CsvFileUploadTask<T> implements Task {
    private CsvUploadFile csv;
    private CsvRecordConverter<T> converter;
    private CsvRecordProcessor<T> processor;
    
    public CsvFileUploadTask(CsvUploadFile csv, CsvRecordConverter<T> converter, CsvRecordProcessor<T> processor) {
        Objects.requireNonNull(csv);
        Objects.requireNonNull(converter);
        Objects.requireNonNull(processor);
        
        this.csv = csv;
        this.converter = converter;
        this.processor = processor;
    }

    @Override
    public void run() {
        this.csv.each(new ThrowableConsumer<List<String>>() {
            
            @Override
            public void consume(List<String> record) throws Exception {
                T converted = converter.convert(record);
                processor.process(converted);
            }
        });
    }
}
