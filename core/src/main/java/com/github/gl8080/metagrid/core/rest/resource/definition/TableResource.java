package com.github.gl8080.metagrid.core.rest.resource.definition;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.gl8080.metagrid.core.definition.Table;
import com.github.gl8080.metagrid.core.definition.TableList;
import com.github.gl8080.metagrid.core.definition.TableRepository;
import com.github.gl8080.metagrid.core.rest.response.MetaGridResponse;

@Path("table")
public class TableResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MetaGridResponse<List<Table>> getAllTable() {
        TableRepository repo = new TableRepository();
        TableList tables = repo.findAllTables();
        
        List<Table> tableList = new ArrayList<>();
        
        for (Table table : tables) {
            tableList.add(table);
        }
        
        return MetaGridResponse.of(tableList);
    }
}
