package com.github.gl8080.metagrid.core.rest.response;

public class MetaGridResponse<T> {
    
    private T value;
    
    public static <T> MetaGridResponse<T> of(T value) {
        MetaGridResponse<T> response = new MetaGridResponse<>();
        response.value = value;
        return response;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MetaGridResponse [value=" + value + "]";
    }
}
