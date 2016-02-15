package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sql {
    
    private final String text;
    private List<Object> parameterList = new ArrayList<>();
    
    public Sql(String text) {
        Objects.requireNonNull(text);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setParameters(Object... parameters) {
        Objects.requireNonNull(parameters);
        this.parameterList = new ArrayList<>(Arrays.asList(parameters));
    }

    public void setParameterList(List<Object> parameters) {
        this.parameterList = new ArrayList<>(parameters);
    }

    public List<Object> getParameterList() {
        return Collections.unmodifiableList(this.parameterList);
    }

    public void prependParameter(Object parameter) {
        this.parameterList.add(0, parameter);
    }
}
