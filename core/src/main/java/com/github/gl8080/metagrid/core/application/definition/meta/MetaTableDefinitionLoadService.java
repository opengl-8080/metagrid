package com.github.gl8080.metagrid.core.application.definition.meta;

import java.util.List;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinitionRepository;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.exception.ActualTableNotFoundException;
import com.github.gl8080.metagrid.core.util.ComponentLoader;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class MetaTableDefinitionLoadService implements ThrowableConsumer<List<String>> {

    @Override
    public void consume(List<String> values) throws Exception {
        ActualTableDefinition actualTableDefinition = this.findActualTableDefinition(values.get(0));
        
        MetaTableDefinition def = MetaTableDefinition.from(actualTableDefinition);
        def.setLogicalName(values.get(1));
        
        MetaTableDefinitionRepository repository = ComponentLoader.getComponent(MetaTableDefinitionRepository.class);
        repository.register(def);
    }
    
    private ActualTableDefinition findActualTableDefinition(String physicalName) {
        ActualTableDefinitionRepository actualRepo = ComponentLoader.getComponent(ActualTableDefinitionRepository.class);
        ActualTableDefinition actualTableDefinition = actualRepo.findByPhysicalName(physicalName);
        
        if (actualTableDefinition == null) {
            throw new ActualTableNotFoundException(physicalName);
        }
        
        return actualTableDefinition;
    }
}
