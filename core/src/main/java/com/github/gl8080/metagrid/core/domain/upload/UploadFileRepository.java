package com.github.gl8080.metagrid.core.domain.upload;

public interface UploadFileRepository {
    
    int register(UploadFile uploadFile);
    
    int update(UploadFile uploadFile);
}
