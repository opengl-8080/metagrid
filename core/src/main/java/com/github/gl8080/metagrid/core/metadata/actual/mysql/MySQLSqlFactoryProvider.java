package com.github.gl8080.metagrid.core.metadata.actual.mysql;

import com.github.gl8080.metagrid.core.metadata.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.metadata.actual.TableListSqlFactory;

public class MySQLSqlFactoryProvider extends SqlFactoryProvider {
    
    @Override
    public TableListSqlFactory getTableListSqlFactory() {
        return new MySQLTableListSqlFactory();
    }
}
