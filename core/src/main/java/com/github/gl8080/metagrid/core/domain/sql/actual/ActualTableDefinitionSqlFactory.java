package com.github.gl8080.metagrid.core.domain.sql.actual;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public interface ActualTableDefinitionSqlFactory {

    Sql createSelectAllSql();
}
