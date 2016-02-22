package com.github.gl8080.metagrid.core.upload;

public interface UploadFileRepository {
    
    int register(UploadFile uploadFile);
    
    int update(UploadFile uploadFile);
}
