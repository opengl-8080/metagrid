package com.github.gl8080.metagrid.core.infrastructure.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.SqlProcessor;
import com.github.gl8080.metagrid.core.util.Supplier;

public abstract class AbstractActualTableDefinitionSqlFactory implements ActualTableDefinitionSqlFactory {
    
    protected static final String PHYSICAL_NAME = "PHYSICAL_NAME";
    protected static final String LOGICAL_NAME = "LOGICAL_NAME";

    @Override
    public Sql<List<ActualTableDefinition>> createSelectAllSql() {
        return new Sql<>(this.getSelectAllSqlText(), new GetTableListProcessor());
    }
    
    abstract protected String getSelectAllSqlText();
    
    private static class TableListProcessor implements SqlProcessor<List<ActualTableDefinition>> {
        
        private List<ActualTableDefinition> tableList = new ArrayList<>();
        
        @Override
        public void consume(ResultSet rs) throws Exception {
            String tableName = rs.getString(PHYSICAL_NAME);
            String comments = rs.getString(LOGICAL_NAME);
            ActualTableDefinition actual = ActualTableDefinition.of(tableName).logicalName(comments).build();
            this.tableList.add(actual);
        }

        @Override
        public List<ActualTableDefinition> getResult() {
            return this.tableList;
        }
    }
    
    private static class GetTableListProcessor implements Supplier<SqlProcessor<List<ActualTableDefinition>>> {

        @Override
        public SqlProcessor<List<ActualTableDefinition>> get() {
            return new TableListProcessor();
        }
    }
}
