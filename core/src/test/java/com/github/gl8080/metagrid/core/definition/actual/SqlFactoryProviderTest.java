package com.github.gl8080.metagrid.core.definition.actual;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.gl8080.metagrid.core.definition.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.mysql.MySQLSqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.oracle.OracleSqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class SqlFactoryProviderTest {

    @Mocked
    private JdbcHelper jdbc;
    
    @Test
    public void データベースがOracleの場合_Oracle用のファクトリが生成される() {
        assertGetSearchSqlFactory(DatabaseType.ORACLE, OracleSqlFactoryProvider.class);
    }
    
    @Test
    public void データベースがMySQLの場合_MySQL用のファクトリが生成される() {
        assertGetSearchSqlFactory(DatabaseType.MYSQL, MySQLSqlFactoryProvider.class);
    }
    
    private void assertGetSearchSqlFactory(final DatabaseType type, Class<? extends SqlFactoryProvider> factoryClass) {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = type;
        }};
        
        // exercise
        SqlFactoryProvider searchSqlFactory = SqlFactoryProvider.getProvider();
        
        // verify
        assertThat(searchSqlFactory).isInstanceOf(factoryClass);
    }

}
