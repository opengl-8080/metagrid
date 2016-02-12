package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TemplateTest {

    @Rule
    public ExpectedException ex = ExpectedException.none();
    private Template template = new Template();
    private Map<String, Object> parameter = new HashMap<>();
    
    @Test
    public void テンプレートにパラメータを埋め込んだ文字列を取得できる() {
        // setup
        parameter.put("test", "FOO");
        
        // exercise
        template.setTemplate("test ${test}");
        String text = template.render(parameter);
        
        // verify
        assertThat(text).isEqualTo("test FOO");
    }
    
    @Test
    public void パラメータは大文字小文字を区別する() {
        // setup
        parameter.put("test", "FOO");
        
        // exercise
        template.setTemplate("test ${Test}");
        String text = template.render(parameter);
        
        // verify
        assertThat(text).isEqualTo("test ${Test}");
    }
    
    @Test
    public void パラメータにマッチする箇所が複数ある場合は_全て置換されること() {
        // setup
        parameter.put("test", "FOO");
        
        // exercise
        template.setTemplate("test ${test} ${test}");
        String text = template.render(parameter);
        
        // verify
        assertThat(text).isEqualTo("test FOO FOO");
    }
    
    @Test
    public void 置換するパラメータが複数ある場合() {
        // setup
        parameter.put("test1", "FOO");
        parameter.put("test2", 123);
        
        // exercise
        template.setTemplate("test ${test1} ${test2}");
        String text = template.render(parameter);
        
        // verify
        assertThat(text).isEqualTo("test FOO 123");
    }
    
    @Test
    public void パラメータの値がnullの場合は空文字で置換される() {
        // setup
        parameter.put("test", null);
        
        // exercise
        template.setTemplate("test ${test}");
        String text = template.render(parameter);
        
        // verify
        assertThat(text).isEqualTo("test ");
    }

    @Test
    public void テンプレートテキストにnullは渡せない() {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        template.setTemplate(null);
    }

    @Test
    public void パラメータにnullは渡せない() {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        template.setTemplate("");
        template.render(null);
    }

    @Test
    public void テンプレートを設定していない状態でレンダリングメソッドを読んだら例外がスローされる() {
        // verify
        ex.expect(IllegalStateException.class);
        
        // exercise
        template.render(parameter);
    }
}
