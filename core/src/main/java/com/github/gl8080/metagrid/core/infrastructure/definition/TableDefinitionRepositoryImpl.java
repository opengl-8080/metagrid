package com.github.gl8080.metagrid.core.infrastructure.definition;

import java.util.List;

import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionList;
import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionRepository;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.sql.SqlFactoryProvider;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.sql.SqlFactoryProviders;

public class TableDefinitionRepositoryImpl implements TableDefinitionRepository {

    @Override
    public TableDefinitionList findAllTables() {
        SqlFactoryProvider sqlFactory = SqlFactoryProviders.getSqlFactoryProvider();
        ActualTableDefinitionSqlFactory tableListSqlFactory = sqlFactory.getTableListSqlFactory();
        
        Sql<List<ActualTableDefinition>> selectAll = tableListSqlFactory.createSelectAllSql();
        
        List<ActualTableDefinition> actualTableDefinitionList = selectAll.executeQuery();
        
        return null;
    }
}
