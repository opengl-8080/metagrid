package com.github.gl8080.metagrid.core.infrastructure.jdbc;

public class UpdateResult {

    private int updateCount;
    private long generatedId;

    public int getUpdateCount() {
        return this.updateCount;
    }

    public long getGeneratedId() {
        return this.generatedId;
    }

    void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    void setGeneratedId(long id) {
        this.generatedId = id;
    }

}
