package com.github.gl8080.metagrid.core.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.gl8080.metagrid.core.db.Table;
import com.github.gl8080.metagrid.core.db.TableList;
import com.github.gl8080.metagrid.core.db.TableRepository;
import com.github.gl8080.metagrid.core.db.impl.TableRepositoryImpl;
import com.github.gl8080.metagrid.core.web.response.MetaGridResponse;

@Path("table")
public class TableResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MetaGridResponse<List<Table>> getAllTable() {
        TableRepository repo = new TableRepositoryImpl();
        TableList tables = repo.findAllTables();
        
        List<Table> tableList = new ArrayList<>();
        
        for (Table table : tables) {
            tableList.add(table);
        }
        
        return MetaGridResponse.of(tableList);
    }
}
