package com.github.gl8080.metagrid.core.domain.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import com.github.gl8080.metagrid.core.MetaGridException;

public class UploadFile {
    
    private Long id;
    private String name;
    private File file;
    private RecordCount recordCount;
    private PassedTime processingTime;
    private Status status;
    private ErrorFile errorFile;
    private Charset charset = StandardCharsets.UTF_8;
    
    public UploadFile(String name, File file) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(file);
        
        this.name = name;
        this.file = file;
        this.processingTime = new PassedTime();
        this.status = Status.WAITING;
        this.errorFile = new ErrorFile();
    }

    public void setId(long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public void eachLine(FileLineProcessor processor) {
        try (BufferedReader br = Files.newBufferedReader(this.file.toPath(), this.charset)) {
            String line = null;
            
            while ((line = br.readLine()) != null) {
                processor.process(line);
            }
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setStatus(Status status) {
        Objects.requireNonNull(status);
        this.status = status;
    }
}
