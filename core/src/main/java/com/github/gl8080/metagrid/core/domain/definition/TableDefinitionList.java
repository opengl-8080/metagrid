package com.github.gl8080.metagrid.core.domain.definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class TableDefinitionList implements Iterable<TableDefinition> {
    
    private List<TableDefinition> tables = new ArrayList<>();
    
    public boolean isEmpty() {
        return this.tables.isEmpty();
    }

    public void add(TableDefinition table) {
        Objects.requireNonNull(table);
        this.tables.add(table);
    }

    @Override
    public Iterator<TableDefinition> iterator() {
        return this.tables.iterator();
    }
}
