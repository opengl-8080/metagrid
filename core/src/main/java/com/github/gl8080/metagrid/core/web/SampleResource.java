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
    public String hello() throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(MetagridConfig.getInstance().getDatasource().getJndi());
        
        String sql = "SELECT A.TABLE_NAME, B.COMMENTS FROM USER_TABLES A LEFT JOIN USER_TAB_COMMENTS B ON A.TABLE_NAME=B.TABLE_NAME ORDER BY A.TABLE_NAME";
        
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String comments = rs.getString("COMMENTS");
                
                System.out.println("tableName=" + tableName + ", comments=" + comments);
            }
        }
        
        return "hello sample!!";
    }
}
