package com.github.gl8080.metagrid.core.db;

import com.github.gl8080.metagrid.core.util.Supplier;

public class Sql<R> {
    private static final JdbcHelper jdbc = new JdbcHelper();
    
    private final String text;
    private final Supplier<SqlProcessor<R>> processorSupplier;
    
    public Sql(String text, Supplier<SqlProcessor<R>> processorSupplier) {
        this.text = text;
        this.processorSupplier = processorSupplier;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
    
    public R executeQuery() {
        SqlProcessor<R> sqlProcessor = this.processorSupplier.get();
        
        jdbc.executeQuery(this.text, sqlProcessor);
        
        return sqlProcessor.getResult();
    }
}
