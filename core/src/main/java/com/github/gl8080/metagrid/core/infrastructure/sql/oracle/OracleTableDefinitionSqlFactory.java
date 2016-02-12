package com.github.gl8080.metagrid.core.infrastructure.sql.oracle;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.sql.AbstractActualTableDefinitionSqlFactory;

public class OracleTableDefinitionSqlFactory extends AbstractActualTableDefinitionSqlFactory {

    @Override
    public Sql createSelectAllSql() {
        return new Sql(
                  "    SELECT USER_TABLES.TABLE_NAME " + PHYSICAL_NAME
                + "          ,USER_TAB_COMMENTS.COMMENTS " + LOGICAL_NAME
                + "      FROM USER_TABLES"
                + " LEFT JOIN USER_TAB_COMMENTS"
                + "        ON USER_TAB_COMMENTS.TABLE_NAME = USER_TABLES.TABLE_NAME");
    }
}
