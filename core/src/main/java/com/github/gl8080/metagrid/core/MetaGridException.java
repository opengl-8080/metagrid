package com.github.gl8080.metagrid.core;

public class MetaGridException extends RuntimeException {
    private static final long serialVersionUID = 8341012986972452720L;
    
    public MetaGridException(Throwable cause) {
        super(cause);
    }
    
    public MetaGridException(String message, Throwable cause) {
        super(message, cause);
    }

    public Throwable getRootCause() {
        Throwable cause = this.getCause();
        
        if (cause == null) {
            return this;
        } else {
            return this.getRootCause(cause);
        }
    }
    
    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex.getCause();
        
        if (cause == null) {
            return ex;
        } else {
            return this.getRootCause(cause);
        }
    }
}
