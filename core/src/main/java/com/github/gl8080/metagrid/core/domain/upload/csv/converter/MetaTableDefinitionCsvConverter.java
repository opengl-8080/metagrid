package com.github.gl8080.metagrid.core.domain.upload.csv.converter;

import java.util.List;
import java.util.Objects;

import com.github.gl8080.metagrid.core.domain.upload.csv.CsvRecordProcessor;
import com.github.gl8080.metagrid.core.domain.upload.csv.dto.MetaTableDefinitionCsvRecord;

public class MetaTableDefinitionCsvConverter implements CsvRecordProcessor<List<String>> {
    
    private CsvRecordProcessor<MetaTableDefinitionCsvRecord> processor;
    
    public MetaTableDefinitionCsvConverter(CsvRecordProcessor<MetaTableDefinitionCsvRecord> processor) {
        Objects.requireNonNull(processor);
        this.processor = processor;
    }

    @Override
    public void process(List<String> record) {
        MetaTableDefinitionCsvRecord converted = new MetaTableDefinitionCsvRecord();
        converted.physicalName = record.get(0);
        converted.logicalName = record.get(1);
        
        this.processor.process(converted);
    }

}
