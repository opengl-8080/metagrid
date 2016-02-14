package com.github.gl8080.metagrid.core.domain.upload;

import java.io.File;
import java.util.Objects;

public class UploadFile {
    
    private String name;
    private File file;
    
    public UploadFile(String name, File file) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(file);
        
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }
}
