package com.github.gl8080.metagrid.core.infrastructure.definition.meta;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.util.SqlResolver;

public class MetaTableDefinitionRepositoryImpl implements MetaTableDefinitionRepository {

    @Override
    public int register(MetaTableDefinition def) {
        Objects.requireNonNull(def);
        
        JdbcHelper jdbc = this.getJdbcHelper();
        
        Sql getNextSeqSql = this.resolveSql(jdbc, "getNextSeq");
        BigDecimal nextId = jdbc.querySingle(getNextSeqSql);
        int nextIdInt = nextId.intValue();
        
        Sql registerSql = this.resolveSql(jdbc, "register");
        registerSql.setParameters(nextIdInt, def.getPhysicalName(), def.getLogicalName());
        
        int cnt = jdbc.update(registerSql);
        
        def.setId(nextIdInt);
        
        return cnt;
    }

    @Override
    public MetaTableDefinition toMetaTableDefinition(ActualTableDefinition actualTableDef) {
        JdbcHelper jdbc = this.getJdbcHelper();

        Sql sql = this.resolveSql(jdbc, "findByPhysicalName");
        sql.setParameters(actualTableDef.getPhysicalName());
        
        Map<String, Object> result = jdbc.queryListMap(sql).get(0);
        
        MetaTableDefinition metaTableDefinition = MetaTableDefinition.from(actualTableDef);
        metaTableDefinition.setId(((BigDecimal)result.get("ID")).longValue());
        metaTableDefinition.setLogicalName((String)result.get("LOGICAL_NAME"));
        
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
