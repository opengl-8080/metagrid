package com.github.gl8080.metagrid.core.metadata.actual.oracle;

import com.github.gl8080.metagrid.core.metadata.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.metadata.actual.TableListSqlFactory;

public class OracleSqlFactoryProvider extends SqlFactoryProvider {

    @Override
    public TableListSqlFactory getTableListSqlFactory() {
        return new OracleTableListSqlFactory();
    }
}
