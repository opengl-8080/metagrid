package com.github.gl8080.metagrid.core.rest.convert.csv.definition.meta;

import java.util.List;

import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class MetaTableDefinitionCsvConverter implements ThrowableConsumer<List<String>> {
    
    private ThrowableConsumer<MetaTableDefinitionCsv> csvProcessor;
    
    public MetaTableDefinitionCsvConverter(ThrowableConsumer<MetaTableDefinitionCsv> csvProcessor) {
        this.csvProcessor = csvProcessor;
    }
    
    @Override
    public void consume(List<String> value) throws Exception {
        MetaTableDefinitionCsv csv = new MetaTableDefinitionCsv();
        
        csv.physicalName = value.get(0);
        csv.logicalName = value.get(1);

        csv.validate();
        
        this.csvProcessor.consume(csv);
    }
}
