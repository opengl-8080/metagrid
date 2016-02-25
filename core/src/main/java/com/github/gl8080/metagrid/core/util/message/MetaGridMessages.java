package com.github.gl8080.metagrid.core.util.message;

public enum MetaGridMessages implements MessageCode {
    ERROR("metagrid.error"),
    SUCCESS("metagrid.success")
    ;

    private final String key;
    
    private MetaGridMessages(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
