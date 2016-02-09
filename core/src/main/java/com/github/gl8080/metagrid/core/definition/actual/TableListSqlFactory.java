package com.github.gl8080.metagrid.core.definition.actual;

import java.sql.ResultSet;

import com.github.gl8080.metagrid.core.definition.Table;
import com.github.gl8080.metagrid.core.definition.TableList;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.SqlProcessor;
import com.github.gl8080.metagrid.core.util.Supplier;

public abstract class TableListSqlFactory {
    
    protected static final String PHYSICAL_NAME = "PHYSICAL_NAME";
    protected static final String LOGICAL_NAME = "LOGICAL_NAME";

    public Sql<TableList> createSelectAllSql() {
        return new Sql<>(this.getSelectAllSqlText(), new GetTableListProcessor());
    }
    
    abstract protected String getSelectAllSqlText();
    
    private static class TableListProcessor implements SqlProcessor<TableList> {
        
        private TableList tableList = new TableList();
        
        @Override
        public void consume(ResultSet rs) throws Exception {
            String tableName = rs.getString(PHYSICAL_NAME);
            String comments = rs.getString(LOGICAL_NAME);
            
            tableList.add(Table.of(tableName).logicalName(comments).build());
        }

        @Override
        public TableList getResult() {
            return this.tableList;
        }
    }
    
    private static class GetTableListProcessor implements Supplier<SqlProcessor<TableList>> {

        @Override
        public SqlProcessor<TableList> get() {
            return new TableListProcessor();
        }
    }
}
