package com.github.gl8080.metagrid.core.domain.sql;

import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;

public interface SqlFactoryProvider {

    ActualTableDefinitionSqlFactory getTableListSqlFactory();
}
