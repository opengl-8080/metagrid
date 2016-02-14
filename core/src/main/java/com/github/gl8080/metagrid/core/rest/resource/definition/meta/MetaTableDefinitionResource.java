package com.github.gl8080.metagrid.core.rest.resource.definition.meta;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.application.definition.meta.MetaTableDefinitionLoadService;
import com.github.gl8080.metagrid.core.rest.convert.csv.CsvUploadFile;

@Path("meta-table-definition")
public class MetaTableDefinitionResource {

    @PUT
    @Consumes("text/csv")
    public Response load(InputStream in) throws IOException {
        CsvUploadFile csv = new CsvUploadFile(in);
        
        MetaTableDefinitionLoadService service = new MetaTableDefinitionLoadService();
        
        csv.each(service);
        
        return Response.ok().build();
    }
}
