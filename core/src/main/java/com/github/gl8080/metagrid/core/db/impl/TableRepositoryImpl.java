package com.github.gl8080.metagrid.core.db.impl;

import java.sql.ResultSet;

import com.github.gl8080.metagrid.core.db.Table;
import com.github.gl8080.metagrid.core.db.TableList;
import com.github.gl8080.metagrid.core.db.TableRepository;
import com.github.gl8080.metagrid.core.db.helper.JdbcHelper;
import com.github.gl8080.metagrid.core.util.Consumer;

public class TableRepositoryImpl implements TableRepository {
    
    private static final JdbcHelper jdbc = new JdbcHelper();
    
    @Override
    public TableList findAllTables() {
        String sql = "SELECT TABLE_NAME PHYSICAL_NAME,TABLE_COMMENT LOGICAL_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA <> 'information_schema' ORDER BY TABLE_NAME";
        
        final TableList tableList = new TableList();
        
        jdbc.executeQuery(sql, new Consumer<ResultSet>() {
            
            @Override
            public void consume(ResultSet rs) throws Exception {
                String tableName = rs.getString("PHYSICAL_NAME");
                String comments = rs.getString("LOGICAL_NAME");
                
                tableList.add(Table.of(tableName).logicalName(comments).build());
            }
        });
        
        return tableList;
    }
}
