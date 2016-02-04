package com.github.gl8080.metagrid.core.web.additional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.metadata.additional.AdditionalTableDefinition;
import com.github.gl8080.metagrid.core.metadata.additional.AdditionalTableDefinitionRepository;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;
import com.github.gl8080.metagrid.core.web.convert.csv.CsvUploadFile;

@Path("table-definition")
public class TableDefinitionResource {

    @PUT
    @Consumes("text/csv")
    public Response load(InputStream in) throws IOException {
        CsvUploadFile csv = new CsvUploadFile(in);
        
        csv.each(new ThrowableConsumer<List<String>>() {
            
            @Override
            public void consume(List<String> values) throws Exception {
                
                AdditionalTableDefinition def = new AdditionalTableDefinition();
                def.setPhysicalName(values.get(0));
                def.setLogicalName(values.get(1));
                
                AdditionalTableDefinitionRepository repository = new AdditionalTableDefinitionRepository();
                repository.register(def);
                
            }
        });
        
        return Response.ok().build();
    }
}
