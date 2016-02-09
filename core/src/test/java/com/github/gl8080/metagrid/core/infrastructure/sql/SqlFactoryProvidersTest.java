package com.github.gl8080.metagrid.core.infrastructure.sql;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.sql.SqlFactoryProviders;
import com.github.gl8080.metagrid.core.infrastructure.sql.mysql.MySQLSqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.sql.oracle.OracleSqlFactoryProvider;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class SqlFactoryProvidersTest {

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
        SqlFactoryProvider searchSqlFactory = SqlFactoryProviders.getSqlFactoryProvider();
        
        // verify
        assertThat(searchSqlFactory).isInstanceOf(factoryClass);
    }

}
