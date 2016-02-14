package com.github.gl8080.metagrid.core.infrastructure.jdbc.id;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;

public class GenerateIdStrategyFactory {
    
    private GenerateIdStrategyFactory() {}

    public static GenerateIdStrategy auto() {
        return new AutoGenerateIdStrategy();
    }

    public static GenerateIdStrategy sequence(String sequenceName, JdbcHelper jdbc) {
        switch (jdbc.getDatabaseType()) {
        case ORACLE:
            return new OracleSequenceStrategy(sequenceName, jdbc);
        case POSTGRESQL:
            return new PostgreSQLSequenceStrategy();
        case MYSQL:
            throw new UnsupportedOperationException("MySQL はシーケンスをサポートしていません。");
        default:
            throw new RuntimeException();
        }
    }
}
