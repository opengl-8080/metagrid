package com.github.gl8080.metagrid.core.application.upload;

import com.github.gl8080.metagrid.core.domain.upload.ErrorMessage;
import com.github.gl8080.metagrid.core.domain.upload.ErrorRecord;
import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;
import com.github.gl8080.metagrid.core.domain.upload.FileUploadProcessException;
import com.github.gl8080.metagrid.core.domain.upload.Status;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.util.ComponentLoader;
import com.github.gl8080.metagrid.core.util.message.MetaGridMessages;
import com.github.gl8080.metagrid.core.util.message.ResourceBundleHelper;

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
        UploadFileRepository uploadFileRepository = ComponentLoader.getComponent(UploadFileRepository.class);
        JdbcHelper repository = JdbcHelper.getRepositoryHelper();
        ErrorRecord errorRecord = null;
        
        try {
            this.uploadFile.setStatus(Status.PROCESSING);
            this.uploadFile.getProcessingTime().begin();
            
            this.targetJdbc.beginTransaction();
            this.delegate.process(line);
            this.targetJdbc.commitTransaction();
        } catch (FileUploadProcessException e) {
            this.uploadFile.setStatus(Status.ERROR_END);
            this.targetJdbc.rollbackTransaction();

            errorRecord = new ErrorRecord(line);
            errorRecord.setMessages(e.getErrorMessages());
            
            throw e;
        } catch (Exception e) {
            this.uploadFile.setStatus(Status.ERROR_END);
            this.targetJdbc.rollbackTransaction();
            
            errorRecord = new ErrorRecord(line);
            String errorMessage = ResourceBundleHelper.getInstance().getMessage(MetaGridMessages.ERROR);
            ErrorMessage message = new ErrorMessage(errorMessage);
            errorRecord.addMessage(message);
            
            throw e;
        } finally {
            this.uploadFile.getProcessingTime().end();
            this.uploadFile.getRecordCount().increment();
            
            repository.beginTransaction();
            if (errorRecord == null) {
                uploadFileRepository.update(uploadFile);
            } else {
                uploadFileRepository.addErrorRecord(uploadFile, errorRecord);
            }
            repository.commitTransaction();
        }
    }
}
