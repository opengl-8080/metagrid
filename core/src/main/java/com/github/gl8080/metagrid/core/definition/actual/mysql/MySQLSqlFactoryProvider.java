package com.github.gl8080.metagrid.core.definition.actual.mysql;

import com.github.gl8080.metagrid.core.definition.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.TableListSqlFactory;

public class MySQLSqlFactoryProvider extends SqlFactoryProvider {
    
    @Override
    public TableListSqlFactory getTableListSqlFactory() {
        return new MySQLTableListSqlFactory();
    }
}
