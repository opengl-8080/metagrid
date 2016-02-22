package com.github.gl8080.metagrid.core.upload.csv;

import java.util.List;

public interface CsvRecordProcessor {
    
    void process(List<String> record);
}
