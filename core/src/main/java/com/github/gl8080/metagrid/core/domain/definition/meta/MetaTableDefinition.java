package com.github.gl8080.metagrid.core.domain.definition.meta;

import com.github.gl8080.metagrid.core.domain.definition.TableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;

public class MetaTableDefinition implements TableDefinition {
    
    private Long id;
    private final ActualTableDefinition base;
    
    public static MetaTableDefinition of(ActualTableDefinition base) {
        MetaTableDefinition meta = new MetaTableDefinition(base);
        return meta;
    }
    
    private MetaTableDefinition(ActualTableDefinition base) {
        this.base = base;
    }
    
    void setId(long id) {
        this.id = id;
    }
    
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPhysicalName() {
        return this.base.getPhysicalName();
    }

    @Override
    public String getLogicalName() {
        return this.base.getLogicalName();
    }
}
