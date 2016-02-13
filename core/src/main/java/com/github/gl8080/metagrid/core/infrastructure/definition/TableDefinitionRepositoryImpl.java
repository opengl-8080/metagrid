package com.github.gl8080.metagrid.core.infrastructure.definition;

import java.util.List;

import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionList;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.sql.AbstractActualTableDefinitionSqlFactory.ActualTableDefinitionConverter;
import com.github.gl8080.metagrid.core.infrastructure.sql.SqlFactoryProviders;

@Deprecated
public class TableDefinitionRepositoryImpl {

//    @Override
    public TableDefinitionList findAllTables() {
        SqlFactoryProvider sqlFactory = SqlFactoryProviders.getSqlFactoryProvider();
        ActualTableDefinitionSqlFactory tableListSqlFactory = sqlFactory.getTableListSqlFactory();
        
        Sql selectAll = tableListSqlFactory.createSelectAllSql();
        
        JdbcHelper jdbc = new JdbcHelper(MetagridConfig.getInstance().getRepositoryDataSource());
        
        List<ActualTableDefinition> actualTableDefinitionList =
                jdbc.queryList(selectAll, new ActualTableDefinitionConverter());
        
        return null;
    }
}
