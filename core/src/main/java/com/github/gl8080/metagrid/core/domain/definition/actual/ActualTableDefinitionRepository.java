package com.github.gl8080.metagrid.core.domain.definition.actual;

import java.util.List;

public interface ActualTableDefinitionRepository {
    
    List<ActualTableDefinition> findAll();
    ActualTableDefinition findByPhysicalName(String physicalName);
}
