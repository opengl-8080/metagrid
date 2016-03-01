package com.github.gl8080.metagrid.core.domain.upload;

import java.util.ArrayList;
import java.util.List;

public class FileUploadProcessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private List<ErrorMessage> messages = new ArrayList<>();
    
    public void addMessage(ErrorMessage message) {
        this.messages.add(message);
    }

    public List<ErrorMessage> getErrorMessages() {
        return new ArrayList<>(this.messages);
    }
}
