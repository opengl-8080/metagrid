package com.github.gl8080.metagrid.core.definition.actual.oracle;

import com.github.gl8080.metagrid.core.definition.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.TableListSqlFactory;

public class OracleSqlFactoryProvider extends SqlFactoryProvider {

    @Override
    public TableListSqlFactory getTableListSqlFactory() {
        return new OracleTableListSqlFactory();
    }
}
