package com.github.gl8080.metagrid.core.definition.actual.mysql;

import com.github.gl8080.metagrid.core.definition.actual.TableListSqlFactory;

public class MySQLTableListSqlFactory extends TableListSqlFactory {
    
    @Override
    public String getSelectAllSqlText() {
        return "   SELECT TABLE_NAME " + PHYSICAL_NAME
              + "         ,TABLE_COMMENT " + LOGICAL_NAME
              + "     FROM INFORMATION_SCHEMA.TABLES"
              + "    WHERE TABLE_SCHEMA <> 'information_schema'"
              + " ORDER BY TABLE_NAME";
    }
}
