package com.github.gl8080.metagrid.core.infrastructure.jdbc;

public class Sql {
    
    private final String text;
    private Object[] parameters = new Object[0];
    
    public Sql(String string) {
        this.text = string;
    }

    public String getText() {
        return this.text;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return this.parameters;
    }
}
