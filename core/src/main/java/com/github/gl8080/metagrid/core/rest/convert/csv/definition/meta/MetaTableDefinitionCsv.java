package com.github.gl8080.metagrid.core.rest.convert.csv.definition.meta;

import com.github.gl8080.metagrid.core.upload.csv.CsvFormatException;

public class MetaTableDefinitionCsv {
    
    public String physicalName;
    public String logicalName;
    
    public void validate() {
        if (this.physicalName == null || this.physicalName.isEmpty()) {
        }
    }
    
}
