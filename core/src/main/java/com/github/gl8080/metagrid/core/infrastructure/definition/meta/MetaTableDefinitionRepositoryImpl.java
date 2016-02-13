package com.github.gl8080.metagrid.core.infrastructure.definition.meta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.UpdateResult;
import com.github.gl8080.metagrid.core.util.SqlResolver;

public class MetaTableDefinitionRepositoryImpl implements MetaTableDefinitionRepository {

    @Override
    public int register(MetaTableDefinition def) {
        Objects.requireNonNull(def);
        
        JdbcHelper jdbc = this.getJdbcHelper();
        int cnt;
        
        if (jdbc.getDatabaseType().equals(DatabaseType.ORACLE)) {
            Sql getNextSeqSql = this.resolveSql(jdbc, "getNextSeq");
            BigDecimal nextId = jdbc.querySingle(getNextSeqSql);
            int nextIdInt = nextId.intValue();
            
            Sql registerSql = this.resolveSql(jdbc, "register");
            registerSql.setParameters(nextIdInt, def.getPhysicalName(), def.getLogicalName());
            
            cnt = jdbc.update(registerSql).getUpdateCount();
            
            def.setId(nextIdInt);
        } else {
            Sql registerSql = this.resolveSql(jdbc, "register");
            registerSql.setParameters(def.getPhysicalName(), def.getLogicalName());
            
            UpdateResult result = jdbc.update(registerSql);
            
            cnt = result.getUpdateCount();
            def.setId(result.getGeneratedId());
        }
        
        return cnt;
    }

    @Override
    public MetaTableDefinition toMetaTableDefinition(ActualTableDefinition actualTableDef) {
        JdbcHelper jdbc = this.getJdbcHelper();

        Sql sql = this.resolveSql(jdbc, "findByPhysicalName");
        sql.setParameters(actualTableDef.getPhysicalName());

        MetaTableDefinition metaTableDefinition = MetaTableDefinition.from(actualTableDef);
        
        List<Map<String,Object>> list = jdbc.queryListMap(sql);
        
        if (!list.isEmpty()) {
            Map<String, Object> result = jdbc.queryListMap(sql).get(0);

            Object id = result.get("ID");
            
            if (id instanceof BigDecimal) {
                metaTableDefinition.setId(((BigDecimal)id).longValue());
            } else if (id instanceof Integer) {
                metaTableDefinition.setId((Integer)id);
            }
            
            metaTableDefinition.setLogicalName((String)result.get("LOGICAL_NAME"));
        }
        
        return metaTableDefinition;
    }
    
    private JdbcHelper getJdbcHelper() {
        DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
        return new JdbcHelper(repositoryConfig);
    }
    
    private Sql resolveSql(JdbcHelper jdbc, String fileName) {
        return new SqlResolver().resolve(jdbc.getDatabaseType(), MetaTableDefinitionRepository.class, fileName);
    }
}
