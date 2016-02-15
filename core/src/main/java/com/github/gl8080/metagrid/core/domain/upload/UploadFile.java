package com.github.gl8080.metagrid.core.domain.upload;

import java.io.File;
import java.util.Objects;

public class UploadFile {
    
    private String name;
    private File file;
    private RecordCount recordCount;
    private PassedTime processingTime;
    private Status status;
    private ErrorFile errorFile;
    
    public UploadFile(String name, File file) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(file);
        
        this.name = name;
        this.file = file;
        this.processingTime = new PassedTime();
        this.status = Status.WAITING;
        this.errorFile = new ErrorFile();
    }
    
    public RecordCount getRecordCount() {
        return recordCount;
    }
    public PassedTime getProcessingTime() {
        return processingTime;
    }
    public Status getStatus() {
        return status;
    }
    public ErrorFile getErrorFile() {
        return errorFile;
    }
    public String getName() {
        return name;
    }
    public File getFile() {
        return file;
    }

    public void setRecordCount(RecordCount recordCount) {
        Objects.requireNonNull(recordCount);
        this.recordCount = recordCount;
    }
}
