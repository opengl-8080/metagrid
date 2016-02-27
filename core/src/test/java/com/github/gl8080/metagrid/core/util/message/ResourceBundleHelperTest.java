package com.github.gl8080.metagrid.core.util.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class ResourceBundleHelperTest {
    
    private static Locale defaultLocale;
    private ResourceBundleHelper helper;
    
    @BeforeClass
    public static void setupDefaultLocale() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.JAPAN);
    }
    
    public class ロケール制御 {
        
        public class デフォルトファイルを指定しない場合 {
            
            @Test
            public void ロケール指定なし_デフォルトロケールでメッセージを取得する() throws Exception {
                // setup
                helper = new ResourceBundleHelper("test_messages");
                
                // exercise
                String message = helper.getMessage(TestMessageCode.TEST_MESSAGE);
                
                // verify
                assertThat(message).isEqualTo("this is ja test message.");
            }
            
            @Test
            public void ロケール指定なし_指定したロケールでメッセージを取得する() throws Exception {
                // setup
                helper = new ResourceBundleHelper(Locale.ENGLISH, "test_messages");
                
                // exercise
                String message = helper.getMessage(TestMessageCode.TEST_MESSAGE);
                
                // verify
                assertThat(message).isEqualTo("this is en test message.");
            }
            
            @Test
            public void メッセージを取得するときに指定したロケールが最優先となる() throws Exception {
                // setup
                helper = new ResourceBundleHelper(Locale.ENGLISH, "test_messages");
                
                // exercise
                String message = helper.getMessage(Locale.JAPAN, TestMessageCode.TEST_MESSAGE);
                
                // verify
                assertThat(message).isEqualTo("this is ja test message.");
            }
            
            @Test
            public void パラメータがあるときも_ロケールを指定できる() throws Exception {
                // setup
                helper = new ResourceBundleHelper(Locale.ENGLISH, "test_messages");
                
                // exercise
                String message = helper.getMessage(Locale.JAPAN, TestMessageCode.BIND_PARAMETER, "foo", "bar");
                
                // verify
                assertThat(message).isEqualTo("foo : bar");
            }
        }
        
        public class デフォルトファイル指定 {
            
            public class ロケールを指定しない場合 {
                
                @Before
                public void setup() {
                    helper = new ResourceBundleHelper("test_default_messages", "test_custom_messages");
                }
                
                @Test
                public void カスタムのデフォルトロケールから値が取得できる() throws Exception {
                    // exercise
                    String message = helper.getMessage(TestMessageCode.CUSTOM_ONLY_MESSAGE);
                    
                    // verify
                    assertThat(message).isEqualTo("custom ja only message");
                }
                
                @Test
                public void デフォルトファイルのデフォルトロケールから値が取得できる() throws Exception {
                    // exercise
                    String message = helper.getMessage(TestMessageCode.DEFAULT_ONLY_MESSAGE);
                    
                    // verify
                    assertThat(message).isEqualTo("default ja only message");
                }
            }
            
            public class ロケールを指定した場合 {

                @Before
                public void setup() {
                    helper = new ResourceBundleHelper(Locale.ENGLISH, "test_default_messages", "test_custom_messages");
                }
                
                @Test
                public void カスタムの指定したロケールから値が取得できる() throws Exception {
                    // exercise
                    String message = helper.getMessage(TestMessageCode.CUSTOM_ONLY_MESSAGE);
                    
                    // verify
                    assertThat(message).isEqualTo("custom en only message");
                }
                
                @Test
                public void デフォルトファイルの指定したロケールから値が取得できる() throws Exception {
                    // exercise
                    String message = helper.getMessage(TestMessageCode.DEFAULT_ONLY_MESSAGE);
                    
                    // verify
                    assertThat(message).isEqualTo("default en only message");
                }
            }
        }
    }
    
    
    public class デフォルトバンドルファイルの制御 {
        
        @Before
        public void setup() {
            helper = new ResourceBundleHelper("default_messages", "custom_messages");
        }

        @Test
        public void 両方に定義されているキーの場合は_カスタム定義のメッセージが取得できる() {
            // exercise
            String message = helper.getMessage(TestMessageCode.DUPLICATED_MESSAGE);
            
            // verify
            assertThat(message).isEqualTo("custom duplicated message");
        }

        @Test
        public void デフォルトの方にしか定義されていないキーの場合は_デフォルト定義のメッセージが取得できる() {
            // exercise
            String message = helper.getMessage(TestMessageCode.DEFAULT_ONLY_MESSAGE);
            
            // verify
            assertThat(message).isEqualTo("default only message");
        }

        @Test
        public void カスタムの方にしか定義されていないキーの場合は_カスタム定義のメッセージが取得できる() {
            // exercise
            String message = helper.getMessage(TestMessageCode.CUSTOM_ONLY_MESSAGE);
            
            // verify
            assertThat(message).isEqualTo("custom only message");
        }
        
        @Test
        public void カスタムファイルが存在しなくて_デフォルトに定義されているキーの場合は_デフォルトのメッセージが取得できる() throws Exception {
            // setup
            helper = new ResourceBundleHelper("default_messages", "not exists");

            // exercise
            String message = helper.getMessage(TestMessageCode.DUPLICATED_MESSAGE);
            
            // verify
            assertThat(message).isEqualTo("default duplicated message");
        }
    }

    @Test
    public void パラメータの埋め込みができる() {
        // setup
        helper = new ResourceBundleHelper("bind_parameter");
        
        // exercise
        String message = helper.getMessage(TestMessageCode.BIND_PARAMETER, "foo", 123);
        
        // verify
        assertThat(message).isEqualTo("0=foo 1=123 0=foo");
    }
    
    @AfterClass
    public static void teardownDefaultLocale() {
        Locale.setDefault(defaultLocale);
    }
    
    
}
