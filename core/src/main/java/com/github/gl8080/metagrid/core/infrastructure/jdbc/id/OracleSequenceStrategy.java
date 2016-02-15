package com.github.gl8080.metagrid.core.infrastructure.jdbc.id;

import java.math.BigDecimal;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public class OracleSequenceStrategy implements SequenceIdStrategy {
    
    private String sequenceName;
    private JdbcHelper jdbc;
    
    public OracleSequenceStrategy(String sequenceName, JdbcHelper jdbc) {
        this.sequenceName = sequenceName;
        this.jdbc = jdbc;
    }

    @Override
    public long generate() {
        Sql sql = new Sql("SELECT " + this.sequenceName + ".NEXTVAL FROM DUAL");
        BigDecimal id = this.jdbc.querySingle(sql);
        return id.longValue();
    }
}
