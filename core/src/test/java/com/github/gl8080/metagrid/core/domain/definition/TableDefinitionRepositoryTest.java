package com.github.gl8080.metagrid.core.domain.definition;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.infrastructure.definition.actual.ActualTableDefinitionRepositoryImpl;
import com.github.gl8080.metagrid.core.infrastructure.definition.meta.MetaTableDefinitionRepositoryImpl;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class TableDefinitionRepositoryTest {
    
    @Mocked
    private ActualTableDefinitionRepositoryImpl actualRepo;
    @Mocked
    private MetaTableDefinitionRepositoryImpl metaRepo;
    
    private List<ActualTableDefinition> actualDefList = new ArrayList<>();
    private Map<String, MetaTableDefinition> metaDefMap = new HashMap<>();
    
    private TableDefinitionRepository repository;
    
    @Before
    public void setup() {
        repository = new TableDefinitionRepository();
    }
    
    @Test
    public void 実定義とメタ定義を組み合わせたテーブル定義一覧が習得できる() {
        // setup
        final ActualTableDefinition actual1 = actualDef("TEST1");
        final ActualTableDefinition actual2 = actualDef("TEST2");
        final ActualTableDefinition actual3 = actualDef("TEST3");
        final MetaTableDefinition meta1 = metaDef("TEST1", "test1");
        final MetaTableDefinition meta2 = metaDef("TEST2", "test2");
        final MetaTableDefinition meta3 = metaDef("TEST3", "test3");
        
        actualDefList.add(actual1);
        actualDefList.add(actual2);
        actualDefList.add(actual3);
        
        metaDefMap.put("TEST3", meta3);
        metaDefMap.put("TEST1", meta1);
        metaDefMap.put("TEST2", meta2);
        
        new NonStrictExpectations() {{
            actualRepo.findAll(); result = actualDefList;
            metaRepo.toMetaTableDefinition(actual1); result = meta1;
            metaRepo.toMetaTableDefinition(actual2); result = meta2;
            metaRepo.toMetaTableDefinition(actual3); result = meta3;
        }};
        
        // exercise
        TableDefinitionList tables = repository.findAllTables();
        
        // verify
        assertThat(tables).as("物理名").extracting("physicalName").containsExactly("TEST1", "TEST2", "TEST3");
        assertThat(tables).as("論理名").extracting("logicalName").containsExactly("test1", "test2", "test3");
        
    }
    
    public static ActualTableDefinition actualDef(String physicalName) {
        return ActualTableDefinition.of(physicalName).build();
    }
    
    public static MetaTableDefinition metaDef(String physicalName, String logicalName) {
        ActualTableDefinition actualDef = actualDef(physicalName);
        MetaTableDefinition metaDef = MetaTableDefinition.from(actualDef);
        metaDef.setLogicalName(logicalName);
        
        return metaDef;
    }
}
