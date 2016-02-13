package com.github.gl8080.metagrid.core.domain.definition.meta;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;

public interface MetaTableDefinitionRepository {

    int register(MetaTableDefinition def);
    MetaTableDefinition toMetaTableDefinition(ActualTableDefinition actualTableDef);
}
