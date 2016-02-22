package com.github.gl8080.metagrid.core.upload.csv;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.upload.csv.CsvRecordParser;
import com.github.gl8080.metagrid.core.upload.csv.CsvRecordProcessor;

public class CsvRecordParserTest {
    
    private CsvRecordParser parser;
    private TestProcessor processor;
    
    
    @Before
    public void setup() {
        processor = new TestProcessor();
        parser = new CsvRecordParser(processor);
    }
    
    @Test
    public void 指定したInputStreamから順次行を取得して_要素に分割したリストをプロセッサに渡す() throws Exception {
        // exercise
        parser.process("a,b,c");
        
        // verify
        processor.assertContains("a", "b", "c");
    }
    
    @Test
    public void 末尾の空白も処理されること() throws Exception {
        // exercise
        parser.process("a,b,");
        
        // verify
        processor.assertContains("a", "b", "");
    }
    
    @Test
    public void 括り文字であるダブルクォーテーションは除去されること() throws Exception {
        // exercise
        parser.process("\"abc\",\"def\"");
        
        // verify
        processor.assertContains("abc", "def");
    }
    
    @Test
    public void 括り文字の中にある二重ダブルクォーテーションは_１つに変換される() throws Exception {
        // exercise
        parser.process("\"a\"\"bc\",def");
        
        // verify
        processor.assertContains("a\"bc", "def");
    }
    
    @Test
    public void 括り文字の中にあるカンマは区切り文字としては認識されないこと() throws Exception {
        // exercise
        parser.process("\"a,bc\",def");
        
        // verify
        processor.assertContains("a,bc", "def");
    }

    private static class TestProcessor implements CsvRecordProcessor {
        
        private List<String> actual;
        
        @Override
        public void process(List<String> elements) {
            this.actual = elements;
        }
        
        public void assertContains(String... expected) {
            assertThat(this.actual).contains(expected);
        }
    }
}
