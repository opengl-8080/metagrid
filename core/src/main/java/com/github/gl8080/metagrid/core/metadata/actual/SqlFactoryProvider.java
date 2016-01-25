package com.github.gl8080.metagrid.core.metadata.actual;

import com.github.gl8080.metagrid.core.db.DatabaseType;
import com.github.gl8080.metagrid.core.db.JdbcHelper;
import com.github.gl8080.metagrid.core.metadata.actual.mysql.MySQLSqlFactoryProvider;
import com.github.gl8080.metagrid.core.metadata.actual.oracle.OracleSqlFactoryProvider;

public abstract class SqlFactoryProvider {
    
    public static SqlFactoryProvider getProvider() {
        JdbcHelper jdbc = new JdbcHelper();
        DatabaseType type = jdbc.getDatabaseType();
        
        switch (type) {
        case ORACLE:
            return new OracleSqlFactoryProvider();
        case MYSQL:
            return new MySQLSqlFactoryProvider();
        case POSTGRESQL:
            throw new UnsupportedOperationException();
        default:
            throw new RuntimeException();
        }
    }
    
    abstract public TableListSqlFactory getTableListSqlFactory();
}
