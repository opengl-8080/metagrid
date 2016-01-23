package com.github.gl8080.metagrid.core.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.db.Table;
import com.github.gl8080.metagrid.core.db.TableList;
import com.github.gl8080.metagrid.core.db.TableRepository;

public class TableRepositoryImpl implements TableRepository {

    @Override
    public TableList findAllTables() {
        try {
            String sql = "SELECT TABLE_NAME PHYSICAL_NAME,TABLE_COMMENT LOGICAL_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA <> 'information_schema' ORDER BY TABLE_NAME";
            
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(MetagridConfig.getInstance().getDatasource().getJndi());
            
            TableList tableList = new TableList();
            
            try (Connection con = ds.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery();) {
                
                while (rs.next()) {
                    String tableName = rs.getString("PHYSICAL_NAME");
                    String comments = rs.getString("LOGICAL_NAME");
                    
                    tableList.add(Table.of(tableName).logicalName(comments).build());
                }
            }
            
            return tableList;
        } catch (SQLException | NamingException e) {
            throw new MetaGridException(e);
        }
    }
}
