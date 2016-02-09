package com.github.gl8080.metagrid.core.domain.sql.actual;

import java.util.List;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public interface ActualTableDefinitionSqlFactory {

    Sql<List<ActualTableDefinition>> createSelectAllSql();
}
