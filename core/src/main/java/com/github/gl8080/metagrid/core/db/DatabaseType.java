package com.github.gl8080.metagrid.core.db;

public enum DatabaseType {
    ORACLE,
    MYSQL,
    POSTGRESQL,
    ;
    
    public static DatabaseType of(String productName) {
        return DatabaseType.of(productName.toUpperCase());
    }
}
