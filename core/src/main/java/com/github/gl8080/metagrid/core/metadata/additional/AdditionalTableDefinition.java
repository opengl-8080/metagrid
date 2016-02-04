package com.github.gl8080.metagrid.core.metadata.additional;

public class AdditionalTableDefinition {
    
    private Long id;
    private String physicalName;
    private String logicalName;
    
    public String getPhysicalName() {
        return physicalName;
    }
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }
    public String getLogicalName() {
        return logicalName;
    }
    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }
    void setId(long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
}
