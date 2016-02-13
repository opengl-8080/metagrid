package com.github.gl8080.metagrid.core.infrastructure.definition.actual;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinitionRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.ResultSetConverter;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.util.SqlResolver;

public class ActualTableDefinitionRepositoryImpl implements ActualTableDefinitionRepository {
    
    @Override
    public List<ActualTableDefinition> findAll() {
        JdbcHelper jdbc = this.getJdbcHelper();
        
        Sql sql = this.resolveSql(jdbc, "findAll");
        
        return jdbc.queryList(sql, converter);
    }

    @Override
    public ActualTableDefinition findByPhysicalName(String physicalName) {
        JdbcHelper jdbc = this.getJdbcHelper();
        
        Sql sql = this.resolveSql(jdbc, "findByPhysicalName");
        sql.setParameters(physicalName);
        
        return jdbc.query(sql, converter);
    }

    private JdbcHelper getJdbcHelper() {
        DataSourceConfig conf = MetagridConfig.getInstance().getDefaultDataSource();
        return new JdbcHelper(conf);
    }
    
    private Sql resolveSql(JdbcHelper jdbc, String fileName) {
        return new SqlResolver().resolve(jdbc.getDatabaseType(), ActualTableDefinitionRepository.class, fileName);
    }
    
    private static final ResultSetConverter<ActualTableDefinition> converter = new ResultSetConverter<ActualTableDefinition>() {
        @Override
        public ActualTableDefinition convert(ResultSet rs) throws SQLException {
            String tableName = rs.getString("PHYSICAL_NAME");
            String comments = rs.getString("LOGICAL_NAME");
            return ActualTableDefinition.of(tableName).logicalName(comments).build();
        }
    };
}
