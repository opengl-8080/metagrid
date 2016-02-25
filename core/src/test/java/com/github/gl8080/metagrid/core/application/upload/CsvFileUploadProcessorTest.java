package com.github.gl8080.metagrid.core.application.upload;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.domain.upload.ErrorRecord;
import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.RecordCount;
import com.github.gl8080.metagrid.core.domain.upload.Status;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.domain.upload.csv.CsvProcessException;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.util.ComponentLoader;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import mockit.Delegate;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.VerificationsInOrder;

@RunWith(HierarchicalContextRunner.class)
public class CsvFileUploadProcessorTest {

    public static class Base {

        public UploadFile uploadFile;
        public int processedRecordCount;
        public long processTime;
        
        @Mocked
        public FileLineProcessor delegate;
        @Mocked
        public JdbcHelper jdbc;
        @Mocked
        public JdbcHelper repository;
        @Mocked
        public UploadFileRepository uploadFileRepository;
        @Mocked
        public ComponentLoader loader;
        
        public CsvFileUploadProcessor processor;
        
        @Rule
        public ExpectedException ex = ExpectedException.none();
        public Exception exception = new Exception();
        public CsvProcessException csvProcessException = new CsvProcessException();
        
        @Before
        public void _setup() {
            new NonStrictExpectations() {{
                ComponentLoader.getComponent(UploadFileRepository.class); result = uploadFileRepository;
                JdbcHelper.getRepositoryHelper(); result = repository;
            }};
            
            uploadFile = new UploadFile("test", new File("test"));
            RecordCount recordCount = new RecordCount(100);
            uploadFile.setRecordCount(recordCount);
            processedRecordCount = recordCount.getProcessedCount();
            processTime = uploadFile.getProcessingTime().getTime();
            
            processor = new CsvFileUploadProcessor(uploadFile, delegate, jdbc);
        }
    }
    
    public class 処理結果の記録 extends Base {

        public class 委譲先が異常終了した場合 extends Base {

            @Before
            public void setup() {
                new NonStrictExpectations() {{
                    delegate.process(anyString); result = exception;
                }};
            }
            
            @Test
            public void リポジトリにアップロード結果が反映されていること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                new VerificationsInOrder() {{
                    repository.beginTransaction();
                    uploadFileRepository.addErrorRecord(uploadFile, (ErrorRecord)any);
                    repository.commitTransaction();
                }};
            }
            
            @Test
            public void エラーコードには生の行データが登録されていること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                new Verifications() {{
                    ErrorRecord errorRecord;
                    uploadFileRepository.addErrorRecord(uploadFile, errorRecord = withCapture());
                    
                    assertThat(errorRecord.getContents()).isEqualTo("test");
                }};
            }
            
            @Test
            public void レコード件数が加算されていること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                assertThat(uploadFile.getRecordCount().getProcessedCount()).isEqualTo(processedRecordCount + 1);
            }
            
            @Test
            public void 処理時間が経過していること() throws Exception {
                // setup
                new NonStrictExpectations() {{
                    delegate.process(anyString);
                    result = new Delegate<Void>() {
                        @SuppressWarnings("unused")
                        public void delegate() throws Exception {
                            Thread.sleep(1L); // 一瞬止めないと、早く終わりすぎて時間経過に差が生まれない
                        }
                    };
                }};
                
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                assertThat(uploadFile.getProcessingTime().getTime()).isGreaterThan(processTime);
            }
            
            @Test
            public void 状態がエラー終了になっていること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                assertThat(uploadFile.getStatus()).isEqualTo(Status.ERROR_END);
            }
        }
        
        public class 委譲先が正常終了した場合 extends Base {

            @Test
            public void リポジトリにアップロード結果が反映されていること() throws Exception {
                // exercise
                processor.process("test");
                
                // verify
                new Verifications() {{
                    repository.beginTransaction();
                    uploadFileRepository.update(uploadFile);
                    repository.commitTransaction();
                }};
            }
            
            @Test
            public void レコード件数が加算されていること() throws Exception {
                // exercise
                processor.process("test");
                
                // verify
                assertThat(uploadFile.getRecordCount().getProcessedCount()).isEqualTo(processedRecordCount + 1);
            }
            
            @Test
            public void 処理時間が経過していること() throws Exception {
                // setup
                new NonStrictExpectations() {{
                    delegate.process(anyString);
                    result = new Delegate<Void>() {
                        @SuppressWarnings("unused")
                        public void delegate() throws Exception {
                            Thread.sleep(1L); // 一瞬止めないと、早く終わりすぎて時間経過に差が生まれない
                        }
                    };
                }};
                
                // exercise
                processor.process("test");
                
                // verify
                assertThat(uploadFile.getProcessingTime().getTime()).isGreaterThan(processTime);
            }
            
            @Test
            public void 状態が処理中になっていること() throws Exception {
                // exercise
                processor.process("test");
                
                // verify
                assertThat(uploadFile.getStatus()).isEqualTo(Status.PROCESSING);
            }
        }
    }
    
    public class トランザクション境界のテスト extends Base {
        
        @Test
        public void 指定したJdbcHelperが前後に境界を設けている() throws Exception {
            // exercise
            processor.process("test");
            
            // verify
            new VerificationsInOrder() {{
                jdbc.beginTransaction();
                delegate.process("test");
                jdbc.commitTransaction();
            }};
        }
        
        public class 委譲先がCSV処理例外をスローした場合 extends Base {

            @Before
            public void setup() {
                new NonStrictExpectations() {{
                    delegate.process(anyString); result = csvProcessException;
                }};
            }

            @Test
            public void ロールバックが実行されること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                new VerificationsInOrder() {{
                    jdbc.beginTransaction();
                    delegate.process("test");
                    jdbc.rollbackTransaction();
                }};
            }
            
            @Test
            public void スローされた例外がそのままスローされ直されていること() throws Exception {
                // verify
                ex.expect(is(csvProcessException));
                
                // exercise
                processor.process("test");
            }
        }
        
        public class 委譲先が例外をスローした場合 extends Base {
            
            @Before
            public void setup() {
                new NonStrictExpectations() {{
                    delegate.process(anyString); result = exception;
                }};
            }
            
            @Test
            public void ロールバックが実行されること() throws Exception {
                // exercise
                try {
                    processor.process("test");
                } catch (Exception e) {/*ignore*/}
                
                // verify
                new VerificationsInOrder() {{
                    jdbc.beginTransaction();
                    delegate.process("test");
                    jdbc.rollbackTransaction();
                }};
            }
            
            @Test
            public void スローされた例外がそのままスローされ直されていること() throws Exception {
                // verify
                ex.expect(is(exception));
                
                // exercise
                processor.process("test");
            }
        }
    }

}
