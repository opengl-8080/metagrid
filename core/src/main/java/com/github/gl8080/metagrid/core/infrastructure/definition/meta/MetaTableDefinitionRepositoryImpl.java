package com.github.gl8080.metagrid.core.infrastructure.definition.meta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.UpdateResult;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategyFactory;
import com.github.gl8080.metagrid.core.util.SqlResolver;

public class MetaTableDefinitionRepositoryImpl implements MetaTableDefinitionRepository {
    private static final Logger logger = LoggerFactory.getLogger(MetaTableDefinitionRepositoryImpl.class);

    @Override
    public int register(MetaTableDefinition def) {
        Objects.requireNonNull(def);
        
        JdbcHelper jdbc = this.getJdbcHelper();
        Long id = null;
        List<Object> parameters = new ArrayList<>();
        
        if (jdbc.is(DatabaseType.ORACLE)) {
            GenerateIdStrategy strategy = GenerateIdStrategyFactory.sequence("META_TABLE_DEFINITION_SEQ", jdbc);
            id = strategy.generate();
            parameters.add(id);
        }
        
        parameters.add(def.getPhysicalName());
        parameters.add(def.getLogicalName());
        
        Sql registerSql = this.resolveSql(jdbc, "register");
        registerSql.setParameterList(parameters);

        UpdateResult result = jdbc.update(registerSql);
        def.setId(id != null ? id : result.getGeneratedId());
        
        return result.getUpdateCount();
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
            } else {
                logger.warn("ID の型が未知の型です。 {}", id.getClass());
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
