package com.github.gl8080.metagrid.core.util;

public interface Consumer<T> {
    
    void consume(T value) throws Exception;
}
