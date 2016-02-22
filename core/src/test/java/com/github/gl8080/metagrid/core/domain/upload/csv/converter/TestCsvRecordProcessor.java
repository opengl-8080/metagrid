package com.github.gl8080.metagrid.core.domain.upload.csv.converter;

import com.github.gl8080.metagrid.core.domain.upload.csv.CsvRecordProcessor;

public class TestCsvRecordProcessor<T> implements CsvRecordProcessor<T> {
    
    public T record;
    
    @Override
    public void process(T record) {
        this.record = record;
    }
}
