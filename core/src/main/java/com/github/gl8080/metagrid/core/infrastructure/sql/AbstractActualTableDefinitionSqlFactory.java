package com.github.gl8080.metagrid.core.infrastructure.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.sql.actual.ActualTableDefinitionSqlFactory;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.ResultSetConverter;

public abstract class AbstractActualTableDefinitionSqlFactory implements ActualTableDefinitionSqlFactory {

    protected static final String PHYSICAL_NAME = "PHYSICAL_NAME";
    protected static final String LOGICAL_NAME = "LOGICAL_NAME";
    
    public static class ActualTableDefinitionConverter implements ResultSetConverter<ActualTableDefinition> {

        @Override
        public ActualTableDefinition convert(ResultSet rs) throws SQLException {
            String tableName = rs.getString(PHYSICAL_NAME);
            String comments = rs.getString(LOGICAL_NAME);
            return ActualTableDefinition.of(tableName).logicalName(comments).build();
        }
    }
}
