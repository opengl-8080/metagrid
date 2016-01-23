package com.github.gl8080.metagrid.core.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class TableList implements Iterable<Table> {
    
    private List<Table> tables = new ArrayList<>();
    
    public boolean isEmpty() {
        return this.tables.isEmpty();
    }

    public void add(Table table) {
        Objects.requireNonNull(table);
        this.tables.add(table);
    }

    @Override
    public Iterator<Table> iterator() {
        return this.tables.iterator();
    }
}
