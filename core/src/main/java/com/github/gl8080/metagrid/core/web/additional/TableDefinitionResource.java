package com.github.gl8080.metagrid.core.web.additional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("table-definition")
public class TableDefinitionResource {
    
    @PUT
    @Produces("text/csv")
    public Response load(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "Shift_JIS"))) {
            String line = null;
            
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        
        return Response.ok().build();
    }
}
