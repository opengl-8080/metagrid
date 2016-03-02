package com.github.gl8080.metagrid.core.domain.upload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.github.gl8080.metagrid.core.util.message.MetaGridMessages;
import com.github.gl8080.metagrid.core.util.message.ResourceBundleHelper;

public class ErrorRecord implements Iterable<ErrorMessage> {
    
    private Long id;
    private final String contents;
    private List<ErrorMessage> messages = new ArrayList<>();
    
    public ErrorRecord(String contents) {
        Objects.requireNonNull(contents);
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Objects.requireNonNull(id);
        this.id = id;
    }
    
    public String getContents() {
        return contents;
    }
    
    public void setMessages(List<ErrorMessage> messages) {
        Objects.requireNonNull(messages);
        this.messages = new ArrayList<>(messages);
    }

    public void addMessage(ErrorMessage message) {
        Objects.requireNonNull(message);
        this.messages.add(message);
    }
    
    public void addSystemErrorMessage() {
        String errorMessage = ResourceBundleHelper.getInstance().getMessage(MetaGridMessages.ERROR);
        ErrorMessage message = new ErrorMessage(errorMessage);
        this.addMessage(message);
    }

    @Override
    public Iterator<ErrorMessage> iterator() {
        return new ArrayList<>(this.messages).iterator();
    }

    public List<ErrorMessage> getMessages() {
        return new ArrayList<>(this.messages);
    }

    public void addError(FileUploadProcessException ex) {
        Objects.requireNonNull(ex);
        this.messages.addAll(ex.getErrorMessages());
    }
}
