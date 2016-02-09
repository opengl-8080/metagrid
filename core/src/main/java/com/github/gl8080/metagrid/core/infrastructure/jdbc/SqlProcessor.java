package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.ResultSet;

import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public interface SqlProcessor<R> extends ThrowableConsumer<ResultSet> {
    
    R getResult();
}
