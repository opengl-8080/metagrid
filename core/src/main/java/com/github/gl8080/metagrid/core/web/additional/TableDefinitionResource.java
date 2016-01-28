package com.github.gl8080.metagrid.core.web.additional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.db.JdbcHelper;

@Path("table-definition")
public class TableDefinitionResource {
    
    @PUT
    @Produces("text/csv")
    public Response load(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "Shift_JIS"))) {
            String line = null;
            
            DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
            JdbcHelper jdbc = new JdbcHelper(repositoryConfig);
            
            while ((line = br.readLine()) != null) {
                int nextId = jdbc.queryInt("SELECT TABLE_DEFINITION_SEQ.NEXTVAL FROM DUAL");
                
                String[] elements = line.split(",");
                
                Object[] parameters = new Object[elements.length + 1];
                parameters[0] = nextId;
                
                for (int i=0; i<elements.length; i++) {
                    String element = elements[i];
                    element = element.replaceAll("^\"|\"$", "").replaceAll("\"\"", "\"");
                    parameters[i + 1] = element;
                }
                
                int count = jdbc.update("INSERT INTO TABLE_DEFINITION (ID, PHYSICAL_NAME, LOGICAL_NAME) VALUES (?, ?, ?)", parameters);
                System.out.println("count=" + count);
            }
        }
        
        return Response.ok().build();
    }
}
