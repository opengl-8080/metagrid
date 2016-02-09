package com.github.gl8080.metagrid.core.definition;

import com.github.gl8080.metagrid.core.definition.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.definition.actual.TableListSqlFactory;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public class TableRepository {

    public TableList findAllTables() {
        SqlFactoryProvider sqlFactory = SqlFactoryProvider.getProvider();
        TableListSqlFactory tableListSqlFactory = sqlFactory.getTableListSqlFactory();
        
        Sql<TableList> selectAll = tableListSqlFactory.createSelectAllSql();
        
        return selectAll.executeQuery();
    }
}
