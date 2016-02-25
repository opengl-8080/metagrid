package com.github.gl8080.metagrid.core.util.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResourceBundleHelperTest {

    private Locale defaultLocal;
    private ResourceBundleHelper helper;
    
    @Before
    public void setup() {
        this.defaultLocal = Locale.getDefault();
        helper = new ResourceBundleHelper("test_messages");
    }
    
    @Test
    public void 指定したリソースバンドルからメッセージが取得できる() {
        // exercise
        String message = helper.getMessage(TestMessageCode.TEST_MESSAGE);
        
        // verify
        assertThat(message).isEqualTo("this is default test message.");
    }
    
    @Test
    public void デフォルトロケールに合わせたファイルが読み込まれること() {
        // setup
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundleHelper helper = new ResourceBundleHelper("test_messages");
        
        // exercise
        String message = helper.getMessage(TestMessageCode.TEST_MESSAGE);
        
        // verify
        assertThat(message).isEqualTo("this is en test message.");
    }
    
    @Test
    public void パラメータの埋め込みができる() {
        // exercise
        String message = helper.getMessage(TestMessageCode.BIND_PARAMETER, "foo", 123);
        
        // verify
        assertThat(message).isEqualTo("0=foo 1=123 0=foo");
    }
    
    @Test
    public void デフォルトの方にしか定義されていないキーの場合は_デフォルト定義のメッセージが取得できる() {
        // setup
        ResourceBundleHelper helper = new ResourceBundleHelper("test_default_messages", "test_custom_messages");
        
        // exercise
        String message = helper.getMessage(TestMessageCode.ONLY_DEFAULT);
        
        // verify
        assertThat(message).isEqualTo("デフォルトだけ");
    }

    @Test
    public void カスタムの方にしか定義されていないキーの場合は_カスタム定義のメッセージが取得できる() {
        // setup
        ResourceBundleHelper helper = new ResourceBundleHelper("test_default_messages", "test_custom_messages");
        
        // exercise
        String message = helper.getMessage(TestMessageCode.ONLY_CUSTOM);
        
        // verify
        assertThat(message).isEqualTo("カスタムだけ");
    }

    @Test
    public void 両方に定義されているキーの場合は_カスタム定義のメッセージが取得できる() {
        // setup
        ResourceBundleHelper helper = new ResourceBundleHelper("test_default_messages", "test_custom_messages");
        
        // exercise
        String message = helper.getMessage(TestMessageCode.DUPLICATE_DEFINED);
        
        // verify
        assertThat(message).isEqualTo("両方定義（カスタム）");
    }
    
    @After
    public void teardown() {
        Locale.setDefault(this.defaultLocal);
    }
    
}
