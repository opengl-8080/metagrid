package com.github.gl8080.metagrid.core.infrastructure.sql;

import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.sql.mysql.MySQLSqlFactoryProvider;
import com.github.gl8080.metagrid.core.infrastructure.sql.oracle.OracleSqlFactoryProvider;

public class SqlFactoryProviders {
    
    public static SqlFactoryProvider getSqlFactoryProvider() {
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
    
    private SqlFactoryProviders() {}
}
