package com.github.gl8080.metagrid.core.application.upload;

import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.Status;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.util.ComponentLoader;

public class CsvFileUploadProcessor implements FileLineProcessor {
    
    private UploadFile uploadFile;
    private FileLineProcessor delegate;
    private JdbcHelper targetJdbc;
    
    public CsvFileUploadProcessor(UploadFile uploadFile, FileLineProcessor delegate, JdbcHelper jdbc) {
        this.uploadFile = uploadFile;
        this.delegate = delegate;
        this.targetJdbc = jdbc;
    }

    @Override
    public void process(String line) {
        try {
            this.uploadFile.setStatus(Status.PROCESSING);
            this.uploadFile.getProcessingTime().begin();
            
            this.targetJdbc.beginTransaction();
            this.delegate.process(line);
            this.targetJdbc.commitTransaction();
            
        } catch (Exception e) {
            this.uploadFile.setStatus(Status.ERROR_END);
            this.targetJdbc.rollbackTransaction();
            throw e;
        } finally {
            this.uploadFile.getProcessingTime().end();
            this.uploadFile.getRecordCount().increment();
            
            UploadFileRepository uploadFileRepository = ComponentLoader.getComponent(UploadFileRepository.class);
            
            JdbcHelper repository = JdbcHelper.getRepositoryHelper();
            repository.beginTransaction();
            uploadFileRepository.update(this.uploadFile);
            repository.commitTransaction();
        }
    }
}
