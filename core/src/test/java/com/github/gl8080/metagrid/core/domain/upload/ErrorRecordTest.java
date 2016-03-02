package com.github.gl8080.metagrid.core.domain.upload;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.util.message.MetaGridMessages;
import com.github.gl8080.metagrid.core.util.message.ResourceBundleHelper;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class ErrorRecordTest {

    
    private static final String ERROR_MESSAGE = "error message";
    private static final String CONTENTS = "foo bar";
    @Mocked
    private ResourceBundleHelper bundle;
    
    private ErrorRecord errorRecord;
    
    @Before
    public void setup() {
        errorRecord = new ErrorRecord(CONTENTS);
        
        new NonStrictExpectations() {{
            bundle.getMessage(MetaGridMessages.ERROR); result = ERROR_MESSAGE;
        }};
    }
    
    @Test
    public void コンストラクタで内容を設定する() {
        // verify
        assertThat(errorRecord.getContents()).isEqualTo(CONTENTS);
    }
    
    @Test
    public void システムエラーを追加すると_デフォルトのエラーメッセージが追加される() throws Exception {
        // exercise
        errorRecord.addSystemErrorMessage();
        
        // verify
        List<ErrorMessage> messages = errorRecord.getMessages();
        
        assertThat(messages).hasSize(1);
        
        ErrorMessage errorMessage = messages.get(0);
        
        assertThat(errorMessage.getFieldName()).as("フィールド名").isNull();
        assertThat(errorMessage.getMessage()).as("メッセージ").isEqualTo(ERROR_MESSAGE);
    }

    @Test
    public void ファイルアップロード例外でエラーメッセージを追加できる() throws Exception {
        // setup
        FileUploadProcessException ex = new FileUploadProcessException();
        ex.addMessage(new ErrorMessage("field1", "message1"));
        ex.addMessage(new ErrorMessage("field2", "message2"));
        ex.addMessage(new ErrorMessage("field3", "message3"));
        
        // exercise
        errorRecord.addError(ex);
        
        // verify
        List<ErrorMessage> messages = errorRecord.getMessages();
        
        assertThat(messages).hasSize(3);
        
        assertThat(messages)
            .as("フィールド名")
            .extracting("fieldName")
            .contains("field1", "field2", "field3");
        
        assertThat(messages)
            .as("メッセージ")
            .extracting("message")
            .contains("message1", "message2", "message3");
    }
}
