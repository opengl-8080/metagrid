package com.github.gl8080.metagrid.core.rest.resource.definition.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.rest.convert.csv.CsvUploadFile;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

@Path("meta-table-definition")
public class MetaTableDefinitionResource {

    @PUT
    @Consumes("text/csv")
    public Response load(InputStream in) throws IOException {
        CsvUploadFile csv = new CsvUploadFile(in);
        
        csv.each(new ThrowableConsumer<List<String>>() {
            
            @Override
            public void consume(List<String> values) throws Exception {
//                
//                MetaTableDefinition def = new MetaTableDefinition();
//                def.setPhysicalName(values.get(0));
//                def.setLogicalName(values.get(1));
//                
//                MetaTableDefinitionRepository repository = new MetaTableDefinitionRepository();
//                repository.register(def);
                
            }
        });
        
        return Response.ok().build();
    }
}
