package com.github.gl8080.metagrid.core.rest.convert.csv.definition.meta;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.gl8080.metagrid.core.rest.convert.csv.CsvFormatException;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

import mockit.Mocked;
import mockit.Verifications;

public class MetaTableDefinitionCsvConverterTest {
    
    @Mocked
    private ThrowableConsumer<MetaTableDefinitionCsv> csvProcessor;
    
    @Rule
    public ExpectedException ex = ExpectedException.none();
    
    private MetaTableDefinitionCsvConverter converter;
    
    @Before
    public void setup() {
        converter = new MetaTableDefinitionCsvConverter(csvProcessor);
    }
    
    @Test
    public void CSVを解析した結果がCSVプロセッサーに渡される() throws Exception {
        // setup
        List<String> values = Arrays.asList("abc", "def");
        
        // exercise
        converter.consume(values);
        
        // verify
        new Verifications() {{
            MetaTableDefinitionCsv csv;
            
            csvProcessor.consume(csv = withCapture());
            
            assertThat(csv.physicalName).as("物理名").isEqualTo("abc");
            assertThat(csv.logicalName).as("論理名").isEqualTo("def");
        }};
    }

    @Test
    public void 物理名が空の場合はエラー() throws Exception {
        // setup
        List<String> values = Arrays.asList("", "def");
        
        // verify
        ex.expect(CsvFormatException.class);
        ex.expectMessage("物理名が空です。");
        
        // exercise
        converter.consume(values);
    }

    @Test
    public void 物理名がnullの場合はエラー() throws Exception {
        // setup
        List<String> values = Arrays.asList(null, "def");
        
        // verify
        ex.expect(CsvFormatException.class);
        ex.expectMessage("物理名が空です。");
        
        // exercise
        converter.consume(values);
    }
}
