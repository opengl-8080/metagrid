package com.github.gl8080.metagrid.core.application.upload;

import com.github.gl8080.metagrid.core.domain.upload.ErrorRecord;
import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.FileUploadProcessException;
import com.github.gl8080.metagrid.core.domain.upload.Status;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.util.ComponentLoader;

public class FileUploadProcessor implements FileLineProcessor {
    
    private UploadFile uploadFile;
    private FileLineProcessor delegate;
    private JdbcHelper targetJdbc;
    
    public FileUploadProcessor(UploadFile uploadFile, FileLineProcessor delegate, JdbcHelper jdbc) {
        this.uploadFile = uploadFile;
        this.delegate = delegate;
        this.targetJdbc = jdbc;
    }

    @Override
    public void process(String line) {
        ErrorRecord errorRecord = null;

        try {
            this.uploadFile.beginProcess();
            
            this.targetJdbc.beginTransaction();
            this.delegate.process(line);
            this.targetJdbc.commitTransaction();
        } catch (FileUploadProcessException e) {
            errorRecord = new ErrorRecord(line);
            errorRecord.addError(e);
            throw e;
        } catch (Exception e) {
            errorRecord = new ErrorRecord(line);
            errorRecord.addSystemErrorMessage();
            throw e;
        } finally {
            this.targetJdbc.rollbackTransaction();
            
            this.uploadFile.endProcess();

            JdbcHelper repository = JdbcHelper.getRepositoryHelper();
            
            repository.beginTransaction();
            this.saveUploadResult(errorRecord);
            repository.commitTransaction();
        }
    }

    private void saveUploadResult(ErrorRecord errorRecord) {
        UploadFileRepository uploadFileRepository = ComponentLoader.getComponent(UploadFileRepository.class);
        
        if (errorRecord == null) {
            uploadFileRepository.update(uploadFile);
        } else {
            this.uploadFile.setStatus(Status.ERROR_END);
            uploadFileRepository.addErrorRecord(uploadFile, errorRecord);
        }
    }
}
