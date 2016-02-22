package com.github.gl8080.metagrid.core.domain.upload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ErrorFile implements Iterable<ErrorRecord> {
    
    private List<ErrorRecord> errorRecords = new ArrayList<>();
    
    public void addErrorRecord(ErrorRecord errorRecord) {
        Objects.requireNonNull(errorRecord);
        this.errorRecords.add(errorRecord);
    }
    
    public void setErrorRecords(List<ErrorRecord> errorRecords) {
        Objects.requireNonNull(errorRecords);
        this.errorRecords = new ArrayList<>(errorRecords);
    }

    @Override
    public Iterator<ErrorRecord> iterator() {
        return new ArrayList<>(errorRecords).iterator();
    }
    
    public boolean isEmpty() {
        return this.errorRecords.isEmpty();
    }
}
