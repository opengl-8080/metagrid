package com.github.gl8080.metagrid.core.domain.definition.meta;

import java.util.Objects;

import com.github.gl8080.metagrid.core.domain.definition.TableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;

public class MetaTableDefinition implements TableDefinition {
    
    private Long id;
    private final ActualTableDefinition base;
    private String logicalName;
    
    private MetaTableDefinition(ActualTableDefinition base) {
        this.base = base;
    }
    
    public MetaTableDefinition() {
        this.base = null;
    }

    public void setId(long id) {
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
        if (this.logicalName == null) {
            return this.base.getLogicalName();
        } else {
            return this.logicalName;
        }
    }

    public static MetaTableDefinition from(ActualTableDefinition actualDef) {
        Objects.requireNonNull(actualDef);
        return new MetaTableDefinition(actualDef);
    }

    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }
}
