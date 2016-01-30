package com.github.gl8080.metagrid.core.web.convert.csv;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

@SuppressWarnings("unchecked")
public class CsvUploadFileTest {
    
    private TestProcessor testProcessor = new TestProcessor();

    @Test
    public void 文字コードを指定しない場合は_デフォルトでUTF_8を使用する() throws Exception {
        // setup
        CsvUploadFile csv = createWithCharset("UTF-8", "あ,い,う");
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(_("あ", "い", "う"));
    }

    @Test
    public void 文字コードを指定できる() throws Exception {
        // setup
        CsvUploadFile csv = createWithCharset("Shift_JIS", "あ,い,う");
        
        // exercise
        csv.each(Charset.forName("Shift_JIS"), testProcessor);
        
        // verify
        testProcessor.assertContains(_("あ", "い", "う"));
    }

    @Test
    public void 指定したInputStreamから順次行を取得して_要素に分割したリストをプロセッサに渡す() throws Exception {
        // setup
        CsvUploadFile csv = create(
            "a,b,c",
            "x,y,z",
            "1,2,3",
            "あ,い,う"
        );
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(
            _("a", "b", "c"),
            _("x", "y", "z"),
            _("1", "2", "3"),
            _("あ", "い", "う")
        );
    }
    
    @Test
    public void 末尾の空白も処理されること() throws Exception {
        // setup
        CsvUploadFile csv = create("a,b,");
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(_("a", "b", ""));
    }
    
    @Test
    public void 括り文字であるダブルクォーテーションは除去されること() throws Exception {
        // setup
        CsvUploadFile csv = create("\"abc\",def");
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(_("abc", "def"));
    }
    
    @Test
    public void 括り文字の中にある二重ダブルクォーテーションは_１つに変換される() throws Exception {
        // setup
        CsvUploadFile csv = create("\"a\"\"bc\",def");
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(_("a\"bc", "def"));
    }
    
    @Test
    public void 括り文字の中にあるカンマは区切り文字としては認識されないこと() throws Exception {
        // setup
        CsvUploadFile csv = create("\"a,bc\",def");
        
        // exercise
        csv.each(testProcessor);
        
        // verify
        testProcessor.assertContains(_("a,bc", "def"));
    }
    
    private static List<String> _(String... strings) {
        return Arrays.asList(strings);
    }
    
    private static CsvUploadFile createWithCharset(String charset, String... csv) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        
        for (String line : csv) {
            sb.append(line).append("\n");
        }
        
        InputStream in = new ByteArrayInputStream(sb.toString().getBytes(charset));
        
        return new CsvUploadFile(in);
    }
    
    
    private static CsvUploadFile create(String... csv) throws UnsupportedEncodingException {
        return createWithCharset("UTF-8", csv);
    }
    
    private static class TestProcessor implements ThrowableConsumer<List<String>> {
        
        private List<List<String>> actual = new ArrayList<>();
        
        @Override
        public void consume(List<String> elements) throws Exception {
            this.actual.add(elements);
        }
        
        public void assertContains(List<String>... expected) {
            assertThat(this.actual).contains(expected);
        }
    }
}
