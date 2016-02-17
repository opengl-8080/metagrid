package com.github.gl8080.metagrid.core.domain.upload.csv;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CsvFileUploadTaskTest {
    
    @Test
    @SuppressWarnings("unchecked")
    public void csvファイルをインプットとして_コンバーターが変換した結果がプロセッサに渡されていること() throws Exception {
        // setup
        String text = "1,2,3\n"
                    + "4,5,6\n"
                    + "7,8,9";
        
        CsvUploadFile csv = new CsvUploadFile(new ByteArrayInputStream(text.getBytes()));
        
        TestCsvRecordProcessor processor = new TestCsvRecordProcessor();
        ToIntegerConverter converter = new ToIntegerConverter();
        
        CsvFileUploadTask<List<Integer>> task = new CsvFileUploadTask<>(csv, converter, processor);
        
        // exercise
        task.run();
        
        // verify
        assertThat(processor.records)
            .containsExactly(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
            );
    }
    
    private class TestCsvRecordProcessor implements CsvRecordProcessor<List<Integer>> {
        
        private List<List<Integer>> records = new ArrayList<>();
        
        @Override
        public void process(List<Integer> record) {
            this.records.add(record);
        }
    }
    
    private class ToIntegerConverter implements CsvRecordConverter<List<Integer>> {

        @Override
        public List<Integer> convert(List<String> record) {
            List<Integer> list = new ArrayList<>();
            
            for (String element : record) {
                list.add(Integer.valueOf(element));
            }
            
            return list;
        }
    }
}
