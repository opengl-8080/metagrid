package com.github.gl8080.metagrid.core.rest.resource.definition;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.gl8080.metagrid.core.domain.definition.TableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionList;
import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionRepository;
import com.github.gl8080.metagrid.core.rest.response.MetaGridResponse;

@Path("table")
public class TableDefinitionResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MetaGridResponse<List<TableDefinition>> getAllTable() {
        TableDefinitionRepository repo = new TableDefinitionRepository();
        TableDefinitionList tables = repo.findAllTables();
        
        List<TableDefinition> tableList = new ArrayList<>();
        
        for (TableDefinition table : tables) {
            tableList.add(table);
        }
        
        return MetaGridResponse.of(tableList);
    }
}
