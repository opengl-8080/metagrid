package com.github.gl8080.metagrid.core.domain.definition.meta;

import java.util.Objects;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public class MetaTableDefinitionRepository {
    
    public int register(MetaTableDefinition def) {
        Objects.requireNonNull(def);
        
        DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
        final JdbcHelper jdbc = new JdbcHelper(repositoryConfig);
        
        try {
            jdbc.beginTransaction();
            
            int nextId = jdbc.querySingle(new Sql("SELECT TABLE_DEFINITION_SEQ.NEXTVAL FROM DUAL"));
            
            Object[] parameters = {nextId, def.getPhysicalName(), def.getLogicalName()};
            
            Sql sql = new Sql("INSERT INTO TABLE_DEFINITION (ID, PHYSICAL_NAME, LOGICAL_NAME) VALUES (?, ?, ?)");
            sql.setParameters(parameters);
            
            int cnt = jdbc.update(sql);
            
            def.setId(nextId);
            
            jdbc.commitTransaction();
            return cnt;
        } catch (Exception e) {
            jdbc.rollbackTransaction();
            throw new MetaGridException(e);
        }
    }
}
