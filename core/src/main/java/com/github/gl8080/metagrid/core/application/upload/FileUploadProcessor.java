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
        ErrorRecord errorRecord = null;

        try {
            this.begin();
            
            this.targetJdbc.beginTransaction();
            this.delegate.process(line);
            this.targetJdbc.commitTransaction();
        } catch (FileUploadProcessException e) {
            errorRecord = this.makeFileUploadErrorRecord(line, e);
            throw e;
        } catch (Exception e) {
            errorRecord = this.makeSystemErrorRecord(line);
            throw e;
        } finally {
            this.targetJdbc.rollbackTransaction();
            
            this.end();

            JdbcHelper repository = JdbcHelper.getRepositoryHelper();
            
            repository.beginTransaction();
            this.saveUploadResult(errorRecord);
            repository.commitTransaction();
        }
    }

    private void begin() {
        this.uploadFile.setStatus(Status.PROCESSING);
        this.uploadFile.getProcessingTime().begin();
    }
    
    private void end() {
        this.uploadFile.getProcessingTime().end();
        this.uploadFile.getRecordCount().increment();
    }

    private ErrorRecord makeFileUploadErrorRecord(String line, FileUploadProcessException e) {
        ErrorRecord errorRecord = new ErrorRecord(line);
        errorRecord.setMessages(e.getErrorMessages());
        return errorRecord;
    }

    private ErrorRecord makeSystemErrorRecord(String line) {
        ErrorRecord errorRecord = new ErrorRecord(line);
        
        String errorMessage = ResourceBundleHelper.getInstance().getMessage(MetaGridMessages.ERROR);
        ErrorMessage message = new ErrorMessage(errorMessage);
        errorRecord.addMessage(message);
        return errorRecord;
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
