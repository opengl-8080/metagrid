package com.github.gl8080.metagrid.core.rest.resource.definition.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinitionRepository;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinitionRepository;
import com.github.gl8080.metagrid.core.exception.ActualTableNotFoundException;
import com.github.gl8080.metagrid.core.rest.convert.csv.CsvUploadFile;
import com.github.gl8080.metagrid.core.util.ComponentLoader;
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
                ActualTableDefinitionRepository actualRepo = ComponentLoader.getComponent(ActualTableDefinitionRepository.class);
                String phsicalName = values.get(0);
                ActualTableDefinition actualTableDefinition = actualRepo.findByPhysicalName(phsicalName);
                
                if (actualTableDefinition == null) {
                    throw new ActualTableNotFoundException(phsicalName);
                }
                
                MetaTableDefinition def = MetaTableDefinition.from(actualTableDefinition);
                def.setLogicalName(values.get(1));
                
                MetaTableDefinitionRepository repository = ComponentLoader.getComponent(MetaTableDefinitionRepository.class);
                repository.register(def);
            }
        });
        
        return Response.ok().build();
    }
}
