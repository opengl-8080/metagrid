package com.github.gl8080.metagrid.core.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.github.gl8080.metagrid.core.config.MetagridConfig;

@Path("sample")
public class SampleResource {
    
    @GET
    public String hello() throws NamingException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(MetagridConfig.instance.getDatasource().getJndi());
        
        System.out.println(ds);
        
        return "hello sample!!";
    }
}
