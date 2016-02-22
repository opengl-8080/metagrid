package com.github.gl8080.metagrid.core.domain.upload.csv.converter;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.domain.upload.csv.dto.MetaTableDefinitionCsvRecord;

public class MetaTableDefinitionCsvConverterTest {
    
    private MetaTableDefinitionCsvConverter converter;
    private TestCsvRecordProcessor<MetaTableDefinitionCsvRecord> processor;

    private List<String> record = Arrays.asList(
        "physical_name",
        "logical_name"
    );
    
    @Before
    public void setup() {
        processor = new TestCsvRecordProcessor<>();
        converter = new MetaTableDefinitionCsvConverter(processor);
    }
    
    @Test
    public void _1列目は物理名() {
        // exercise
        converter.process(record);
        
        // verify
        assertThat(processor.record.physicalName).isEqualTo("physical_name");
    }
    
    @Test
    public void _2列目は物理名() {
        // exercise
        converter.process(record);
        
        // verify
        assertThat(processor.record.logicalName).isEqualTo("logical_name");
    }
}
