package com.github.gl8080.metagrid.core.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("sample")
public class SampleResource {
    
    @GET
    public String hello() {
        return "hello sample";
    }
}
