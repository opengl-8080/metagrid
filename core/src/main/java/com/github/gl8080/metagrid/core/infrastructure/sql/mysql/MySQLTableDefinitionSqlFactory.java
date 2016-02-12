package com.github.gl8080.metagrid.core.infrastructure.sql.mysql;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.sql.AbstractActualTableDefinitionSqlFactory;

public class MySQLTableDefinitionSqlFactory extends AbstractActualTableDefinitionSqlFactory {

    @Override
    public Sql createSelectAllSql() {
        return new Sql(
                  "   SELECT TABLE_NAME " + PHYSICAL_NAME
                + "         ,TABLE_COMMENT " + LOGICAL_NAME
                + "     FROM INFORMATION_SCHEMA.TABLES"
                + "    WHERE TABLE_SCHEMA <> 'information_schema'"
                + " ORDER BY TABLE_NAME");
    }
}
