package com.github.gl8080.metagrid.core.domain.definition;

import java.util.List;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinitionRepository;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.infrastructure.definition.actual.ActualTableDefinitionRepositoryImpl;
import com.github.gl8080.metagrid.core.infrastructure.definition.meta.MetaTableDefinitionRepositoryImpl;

public class TableDefinitionRepository {
    
    public TableDefinitionList findAllTables() {
        ActualTableDefinitionRepository actualRepo = new ActualTableDefinitionRepositoryImpl();
        List<ActualTableDefinition> actualDefList = actualRepo.findAll();
        
        MetaTableDefinitionRepository metaRepo = new MetaTableDefinitionRepositoryImpl();
        
        TableDefinitionList list = new TableDefinitionList();
        
        for (ActualTableDefinition actualDef : actualDefList) {
            MetaTableDefinition metaTableDefinition = metaRepo.toMetaTableDefinition(actualDef);
            list.add(metaTableDefinition);
        }
        
        return list;
    }
}
