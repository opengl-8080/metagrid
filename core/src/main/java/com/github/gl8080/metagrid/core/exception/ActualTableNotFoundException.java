package com.github.gl8080.metagrid.core.exception;

public class ActualTableNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ActualTableNotFoundException(String physicalName) {
        super(physicalName + " というテーブルは存在しません。");
    }
}
