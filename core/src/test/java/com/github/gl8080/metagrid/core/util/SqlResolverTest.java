package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public class SqlResolverTest {

    @Rule
    public ExpectedException ex = ExpectedException.none();

    private SqlResolver resolver = new SqlResolver();
    
    @Test
    public void データベース種別がnullの場合はエラー() throws Exception {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        resolver.resolve(null, Foo.class, "selectAll");
    }
    
    @Test
    public void クラスオブジェクトがnullの場合はエラー() throws Exception {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        resolver.resolve(DatabaseType.ORACLE, null, "selectAll");
    }
    
    @Test
    public void ファイル名がnullの場合はエラー() throws Exception {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        resolver.resolve(DatabaseType.ORACLE, Foo.class, null);
    }
    
    @Test
    public void 条件に一致するファイルの内容を取得してSqlオブジェクトにして返す() throws Exception {
        // exercise
        Sql sql = resolver.resolve(DatabaseType.ORACLE, Foo.class, "selectAll");
        
        // verify
        try (InputStream in = this.getClass().getResourceAsStream("/db/sql/oracle/Foo/selectAll.sql")) {
            String expected = IOUtil.toString(in);
            assertThat(sql.getText()).isEqualTo(expected);
        }
    }
    
    private static class Foo {}
}
