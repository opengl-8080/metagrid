package com.github.gl8080.metagrid.core.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.github.gl8080.metagrid.core.config.MetagridConfig;

@Path("sample")
public class SampleResource {
    
    @GET
    @Path("mysql")
    public String mysql() throws Exception {
        String sql = "SELECT TABLE_NAME PHYSICAL_NAME,TABLE_COMMENT LOGICAL_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA <> 'information_schema' ORDER BY TABLE_NAME";
        return this.getTableNames(sql);
    }
    
    @GET
    @Path("oracle")
    public String oracle() throws Exception {
        String sql = "SELECT A.TABLE_NAME PHYSICAL_NAME, B.COMMENTS LOGICAL_NAME FROM USER_TABLES A LEFT JOIN USER_TAB_COMMENTS B ON A.TABLE_NAME=B.TABLE_NAME ORDER BY A.TABLE_NAME";
        return this.getTableNames(sql);
    }
    
    private String getTableNames(String sql) throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(MetagridConfig.getInstance().getDatasource().getJndi());
        
        StringBuilder sb = new StringBuilder();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            
            while (rs.next()) {
                String tableName = rs.getString("PHYSICAL_NAME");
                String comments = rs.getString("LOGICAL_NAME");

                sb.append("tableName=").append(tableName).append(", comments=").append(comments).append("\r\n");
            }
        }
        
        return sb.toString();
    }
}
