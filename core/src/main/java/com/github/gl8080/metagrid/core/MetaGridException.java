package com.github.gl8080.metagrid.core;

public class MetaGridException extends RuntimeException {
    private static final long serialVersionUID = 8341012986972452720L;
    
    public MetaGridException(Throwable cause) {
        super(cause);
    }
    
    public MetaGridException(String message, Throwable cause) {
        super(message, cause);
    }
}
