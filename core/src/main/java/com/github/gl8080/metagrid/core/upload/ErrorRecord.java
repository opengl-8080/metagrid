package com.github.gl8080.metagrid.core.upload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    @Override
    public Iterator<ErrorMessage> iterator() {
        return new ArrayList<>(this.messages).iterator();
    }
}
