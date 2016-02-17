package com.github.gl8080.metagrid.core.domain.upload.csv;

public interface CsvRecordProcessor<T> {
    
    void process(T record);
}
