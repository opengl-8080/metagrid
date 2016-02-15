package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SqlTest {

    @Rule
    public ExpectedException ex = ExpectedException.none();

    private static final String SQL_TEXT = "select * from test";
    private Sql sql = new Sql(SQL_TEXT);
    
    @Test
    public void SQLテキストを取得できる() {
        // exercise
        String text = sql.getText();
        
        // verify
        assertThat(text).isEqualTo(SQL_TEXT);
    }

    @Test
    public void テキストにnullは指定できない() {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        new Sql(null);
    }

    @Test
    public void パラメータを可変長引数で設定できる() {
        // setup
        sql.setParameters(1, "test", true);
        
        // exercise
        List<Object> parameters = sql.getParameterList();
        
        // verify
        assertThat(parameters).containsExactly(1, "test", true);
    }

    @Test
    public void 可変長引数を受け取るメソッドに配列で渡せる() {
        // setup
        sql.setParameters(new Object[] {1, "test", true});
        
        // exercise
        List<Object> parameters = sql.getParameterList();
        
        // verify
        assertThat(parameters).containsExactly(1, "test", true);
    }

    @Test
    public void パラメータをListで設定できる() {
        // setup
        sql.setParameterList(Arrays.<Object>asList(2, "TEST", false));
        
        // exercise
        List<Object> parameters = sql.getParameterList();
        
        // verify
        assertThat(parameters).containsExactly(2, "TEST", false);
    }

    @Test
    public void パラメータをListで設定するメソッドにnullは渡せない() {
        // verify
        ex.expect(NullPointerException.class);
        
        // setup
        sql.setParameterList(null);
    }

    @Test
    public void パラメータを配列で設定するメソッドにnullは渡せない() {
        // verify
        ex.expect(NullPointerException.class);
        
        // setup
        sql.setParameters((Object[])null);
    }

    @Test
    public void 何もパラメータを設定していない場合は_空の配列が返される() {
        // exercise
        List<Object> parameters = sql.getParameterList();
        
        // verify
        assertThat(parameters).isEmpty();
    }
    
    @Test
    public void 先頭にパラメータを追加できる() throws Exception {
        // setup
        sql.setParameters("a", "b", "c");
        
        // exercise
        sql.prependParameter("D");
        
        // verify
        assertThat(sql.getParameterList()).containsExactly("D", "a", "b", "c");
    }
    
}
