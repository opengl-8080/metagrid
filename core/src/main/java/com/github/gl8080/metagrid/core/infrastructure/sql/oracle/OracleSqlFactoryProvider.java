package com.github.gl8080.metagrid.core.infrastructure.sql.oracle;

import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;

public class OracleSqlFactoryProvider implements SqlFactoryProvider {

    @Override
    public ActualTableDefinitionSqlFactory getTableListSqlFactory() {
        return new OracleTableDefinitionSqlFactory();
    }
}
