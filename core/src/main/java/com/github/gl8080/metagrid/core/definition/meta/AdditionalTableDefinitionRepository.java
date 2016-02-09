package com.github.gl8080.metagrid.core.definition.meta;

import java.util.Objects;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;

public class AdditionalTableDefinitionRepository {
    
    public int register(AdditionalTableDefinition def) {
        Objects.requireNonNull(def);
        
        DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
        final JdbcHelper jdbc = new JdbcHelper(repositoryConfig);
        
        try {
            jdbc.beginTransaction();
            
            int nextId = jdbc.queryInt("SELECT TABLE_DEFINITION_SEQ.NEXTVAL FROM DUAL");
            
            Object[] parameters = {nextId, def.getPhysicalName(), def.getLogicalName()};
            
            int cnt = jdbc.update("INSERT INTO TABLE_DEFINITION (ID, PHYSICAL_NAME, LOGICAL_NAME) VALUES (?, ?, ?)", parameters);
            
            def.setId(nextId);
            
            jdbc.commitTransaction();
            return cnt;
        } catch (Exception e) {
            jdbc.rollbackTransaction();
            throw new MetaGridException(e);
        }
    }
}
