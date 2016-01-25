package com.github.gl8080.metagrid.core.metadata;

import com.github.gl8080.metagrid.core.db.Sql;
import com.github.gl8080.metagrid.core.metadata.actual.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.metadata.actual.TableListSqlFactory;

public class TableRepository {

    public TableList findAllTables() {
        SqlFactoryProvider sqlFactory = SqlFactoryProvider.getProvider();
        TableListSqlFactory tableListSqlFactory = sqlFactory.getTableListSqlFactory();
        
        Sql<TableList> selectAll = tableListSqlFactory.createSelectAllSql();
        
        return selectAll.executeQuery();
    }
}
