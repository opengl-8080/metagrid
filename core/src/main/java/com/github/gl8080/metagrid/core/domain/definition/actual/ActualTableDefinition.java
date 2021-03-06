package com.github.gl8080.metagrid.core.domain.definition.actual;

import java.util.Objects;

public class ActualTableDefinition {
    
    private final String physicalName;
    private String logicalName;
    
    public String getPhysicalName() {
        return physicalName;
    }
    public String getLogicalName() {
        return logicalName;
    }
    
    @Override
    public String toString() {
        return "ActualTableDefinition [physicalName=" + physicalName + ", logicalName=" + logicalName + "]";
    }
    
    protected ActualTableDefinition(String physicalName) {
        Objects.requireNonNull(physicalName, "テーブルの物理名に null は指定できません。");
        if (physicalName.isEmpty()) {
            throw new IllegalArgumentException("テーブルの物理名に空文字は指定できません。");
        }
        this.physicalName = physicalName;
    }
    
    public static class Builder {
        
        private ActualTableDefinition table;
        
        private Builder(String physicalName) {
            this.table = new ActualTableDefinition(physicalName);
        }
        
        public ActualTableDefinition build() {
            return this.table;
        }

        public Builder logicalName(String logicalName) {
            this.table.logicalName = logicalName;
            return this;
        }
        
    }

    public static Builder of(String physicalName) {
        return new Builder(physicalName);
    }
}
