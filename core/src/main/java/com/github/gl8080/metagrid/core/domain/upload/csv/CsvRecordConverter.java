package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.List;

public interface CsvRecordConverter<T> {
    
    T convert(List<String> record);
}
