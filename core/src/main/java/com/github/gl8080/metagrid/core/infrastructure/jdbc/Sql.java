package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.util.List;
import java.util.Objects;

public class Sql {
    
    private final String text;
    private Object[] parameters = new Object[0];
    
    public Sql(String text) {
        Objects.requireNonNull(text);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setParameters(Object... parameters) {
        Objects.requireNonNull(parameters);
        this.parameters = parameters;
    }

    public void setParameterList(List<Object> parameters) {
        this.parameters = parameters.toArray();
    }

    public Object[] getParameters() {
        return this.parameters;
    }
}
