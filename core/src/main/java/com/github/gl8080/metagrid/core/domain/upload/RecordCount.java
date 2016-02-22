package com.github.gl8080.metagrid.core.domain.upload;

public class RecordCount {
    
    private final int totalCount;
    private int processedCount;

    public RecordCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public void increment() {
        this.processedCount++;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public int getProcessedCount() {
        return processedCount;
    }

    @Override
    public String toString() {
        return "RecordCount [totalCount=" + totalCount + ", processedCount=" + processedCount + "]";
    }
}
