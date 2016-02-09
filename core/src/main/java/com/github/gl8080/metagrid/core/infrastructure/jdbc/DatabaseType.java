package com.github.gl8080.metagrid.core.infrastructure.jdbc;

public enum DatabaseType {
    ORACLE,
    MYSQL,
    POSTGRESQL,
    ;
    
    public static DatabaseType of(String productName) {
        return DatabaseType.valueOf(productName.toUpperCase());
    }
}
