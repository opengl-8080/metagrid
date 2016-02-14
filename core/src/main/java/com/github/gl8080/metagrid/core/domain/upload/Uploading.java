package com.github.gl8080.metagrid.core.domain.upload;

import java.util.Objects;

public class Uploading {
    
    private UploadFile uploadFile;
    private RecordCount recordCount;
    private PassedTime processingTime;
    private Status status;
    private ErrorFile errorFile;
    
    public Uploading() {
        this.processingTime = new PassedTime();
        this.status = Status.WAITING;
        this.errorFile = new ErrorFile();
    }
    
    public UploadFile getUploadFile() {
        return uploadFile;
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

    public void setRecordCount(RecordCount recordCount) {
        Objects.requireNonNull(recordCount);
        this.recordCount = recordCount;
    }

    public void setUploadFile(UploadFile uploadFile) {
        Objects.requireNonNull(uploadFile);
        this.uploadFile = uploadFile;
    }
}
