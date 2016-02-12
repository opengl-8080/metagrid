package com.github.gl8080.metagrid.core.infrastructure.jdbc;

public class Sql {
//    private static final JdbcHelper jdbc = new JdbcHelper();
    
    private final String text;
//    private final Supplier<SqlProcessor<R>> processorSupplier;
    private Object[] parameters = new Object[0];
    
//    public Sql(String text, Supplier<SqlProcessor<R>> processorSupplier) {
//        this.text = text;
//        this.processorSupplier = processorSupplier;
//    }
    
    public Sql(String string) {
        this.text = string;
//        this.processorSupplier = null;
    }

//    public R executeQuery() {
//        SqlProcessor<R> sqlProcessor = this.processorSupplier.get();
//        
//        jdbc.query(this.text, sqlProcessor);
//        
//        return sqlProcessor.getResult();
//    }

    public String getText() {
        return this.text;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return this.parameters;
    }
}
