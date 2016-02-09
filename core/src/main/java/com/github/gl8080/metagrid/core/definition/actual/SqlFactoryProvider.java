package com.github.gl8080.metagrid.core.definition.actual;

import com.github.gl8080.metagrid.core.definition.actual.mysql.MySQLSqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.oracle.OracleSqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;

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
