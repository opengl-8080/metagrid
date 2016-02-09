package com.github.gl8080.metagrid.core.definition.actual.oracle;

import com.github.gl8080.metagrid.core.definition.actual.TableListSqlFactory;

public class OracleTableListSqlFactory extends TableListSqlFactory {

    @Override
    public String getSelectAllSqlText() {
        return "    SELECT USER_TABLES.TABLE_NAME " + PHYSICAL_NAME
              + "          ,USER_TAB_COMMENTS.COMMENTS " + LOGICAL_NAME
              + "      FROM USER_TABLES"
              + " LEFT JOIN USER_TAB_COMMENTS"
              + "        ON USER_TAB_COMMENTS.TABLE_NAME = USER_TABLES.TABLE_NAME";
    }
}
