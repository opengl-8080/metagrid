package com.github.gl8080.metagrid.core.upload;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.upload.Status;
import com.github.gl8080.metagrid.core.upload.UploadFile;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class UploadFileTest {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final Charset SHIFT_JIS = Charset.forName("Shift_JIS");
    
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    public class 生成直後の状態 {
        
        private UploadFile uploadFile;
        
        @Before
        public void setup() {
            uploadFile = new UploadFile("fileName", new File("test.txt"));
        }
        
        @Test
        public void 各フィールドの状態() {
            // verify
            assertThat(uploadFile.getRecordCount()).as("処理件数").isNull();
            assertThat(uploadFile.getProcessingTime()).as("処理時間").isNotNull();
            assertThat(uploadFile.getStatus()).as("状態").isNotNull();
            assertThat(uploadFile.getErrorFile()).as("エラーファイル").isNotNull();
        }

        @Test
        public void 状態は_待機() {
            // verify
            assertThat(uploadFile.getStatus()).isSameAs(Status.WAITING);
        }
    }

    public class ファイルの各行処理 {
        
        private UploadFile uploadFile;
        private File file;
        private TestProcessor processor;
        
        private final String content =
                  "あ\n"
                + "い\r\n"
                + "う\n";
        
        @Before
        public void setup() throws Exception {
            File root = tmp.getRoot();
            file = new File(root, "test.txt");
            
            String content = "first\n"
                           + "second\r\n"
                           + "third\n";
            
            writeFile(file, content, UTF_8);
            
            uploadFile = new UploadFile("test.txt", file);
            
            processor = new TestProcessor();
        }
        
        @Test
        public void ファイルの各行を処理できる() throws Exception {
            // exercise
            uploadFile.eachLine(processor);
            
            // verify
            processor.assertContainsExactly("first", "second", "third");
        }

        @Test
        public void 文字コードを指定しない場合は_デフォルトでUTF_8を使用する() throws Exception {
            // setup
            writeFile(file, content, UTF_8);
            
            // exercise
            uploadFile.eachLine(processor);
            
            // verify
            processor.assertContainsExactly("あ", "い", "う");
        }

        @Test
        public void 文字コードを指定できる() throws Exception {
            // setup
            writeFile(file, content, SHIFT_JIS);
            
            uploadFile.setCharset(SHIFT_JIS);
            
            // exercise
            uploadFile.eachLine(processor);
            
            // verify
            processor.assertContainsExactly("あ", "い", "う");
        }
    }
    
    private static class TestProcessor implements FileLineProcessor {

        private final List<String> lines = new ArrayList<>();
        
        @Override
        public void process(String line) {
            this.lines.add(line);
        }
        
        public void assertContainsExactly(String... expected) {
            assertThat(this.lines).containsExactly(expected);
        }
    }
    
    private static void writeFile(File file, String content, Charset charset) throws IOException {
        Files.write(file.toPath(), content.getBytes(charset));
    }
}
