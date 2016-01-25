package com.github.gl8080.metagrid.core.metadata.actual;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.gl8080.metagrid.core.db.DatabaseType;
import com.github.gl8080.metagrid.core.db.JdbcHelper;
import com.github.gl8080.metagrid.core.metadata.actual.mysql.MySQLSearchSqlFactory;
import com.github.gl8080.metagrid.core.metadata.actual.oracle.OracleSearchSqlFactory;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class SearchSqlFactoryTest {

    @Mocked
    private JdbcHelper jdbc;
    
    @Test
    public void データベースがOracleの場合_Oracle用のファクトリが生成される() {
        assertGetSearchSqlFactory(DatabaseType.ORACLE, OracleSearchSqlFactory.class);
    }
    
    @Test
    public void データベースがMySQLの場合_MySQL用のファクトリが生成される() {
        assertGetSearchSqlFactory(DatabaseType.MYSQL, MySQLSearchSqlFactory.class);
    }
    
    private void assertGetSearchSqlFactory(final DatabaseType type, Class<? extends SearchSqlFactory> factoryClass) {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = type;
        }};
        
        // exercise
        SearchSqlFactory searchSqlFactory = SearchSqlFactory.getSearchSqlFactory();
        
        // verify
        assertThat(searchSqlFactory).isInstanceOf(factoryClass);
    }

}
