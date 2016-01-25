package com.github.gl8080.metagrid.core.util;

public interface ThrowableConsumer<T> {
    
    void consume(T value) throws Exception;
}
