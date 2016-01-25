package com.github.gl8080.metagrid.core.web;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

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
        return this.getDatabaseInfomation();
    }
    
    @GET
    @Path("oracle")
    public String oracle() throws Exception {
        return this.getDatabaseInfomation();
    }
    
    private String getDatabaseInfomation() throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(MetagridConfig.getInstance().getDatasource().getJndi());
        
        try (Connection con = ds.getConnection();) {
            DatabaseMetaData metaData = con.getMetaData();
            return metaData.getDatabaseProductName() + " : " + metaData.getDatabaseProductVersion();
        }
    }
}
