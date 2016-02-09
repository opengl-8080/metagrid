package com.github.gl8080.metagrid.core.infrastructure.sql.mysql;

import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;

public class MySQLSqlFactoryProvider implements SqlFactoryProvider {
    
    @Override
    public ActualTableDefinitionSqlFactory getTableListSqlFactory() {
        return new MySQLTableDefinitionSqlFactory();
    }
}
