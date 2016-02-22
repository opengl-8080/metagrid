package com.github.gl8080.metagrid.core.upload;

import java.util.Objects;

public class ErrorMessage {

    private Long id;
    private final String fieldName;
    private final String message;
    
    public ErrorMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Objects.requireNonNull(id);
        this.id = id;
    }
    
    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorMessage [id=" + id + ", fieldName=" + fieldName + ", message=" + message + "]";
    }
}
