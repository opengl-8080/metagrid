package com.github.gl8080.metagrid.core.metadata.actual;

import com.github.gl8080.metagrid.core.db.DatabaseType;
import com.github.gl8080.metagrid.core.db.JdbcHelper;
import com.github.gl8080.metagrid.core.metadata.actual.mysql.MySQLSearchSqlFactory;
import com.github.gl8080.metagrid.core.metadata.actual.oracle.OracleSearchSqlFactory;

public abstract class SearchSqlFactory {
    
    public static SearchSqlFactory getSearchSqlFactory() {
        JdbcHelper jdbc = new JdbcHelper();
        DatabaseType type = jdbc.getDatabaseType();
        
        switch (type) {
        case ORACLE:
            return new OracleSearchSqlFactory();
        case MYSQL:
            return new MySQLSearchSqlFactory();
        case POSTGRESQL:
            throw new UnsupportedOperationException();
        default:
            throw new RuntimeException();
        }
    }
}
