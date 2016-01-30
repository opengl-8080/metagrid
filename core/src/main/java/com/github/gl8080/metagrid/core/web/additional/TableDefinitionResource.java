package com.github.gl8080.metagrid.core.web.additional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.db.JdbcHelper;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;
import com.github.gl8080.metagrid.core.web.convert.csv.CsvUploadFile;

@Path("table-definition")
public class TableDefinitionResource {

    @PUT
    @Consumes("text/csv")
    public Response load(InputStream in) throws IOException {
        CsvUploadFile csv = new CsvUploadFile(in);
        
        DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
        final JdbcHelper jdbc = new JdbcHelper(repositoryConfig);

        try {
            csv.each(new ThrowableConsumer<List<String>>() {
                
                @Override
                public void consume(List<String> value) throws Exception {
                    jdbc.beginTransaction();
                    
                    int nextId = jdbc.queryInt("SELECT TABLE_DEFINITION_SEQ.NEXTVAL FROM DUAL");
                    
                    Object[] parameters = new Object[value.size() + 1];
                    parameters[0] = nextId;
                    
                    for (int i=0; i<value.size(); i++) {
                        parameters[i + 1] = value.get(i);
                    }
                    
                    int cnt = jdbc.update("INSERT INTO TABLE_DEFINITION (ID, PHYSICAL_NAME, LOGICAL_NAME) VALUES (?, ?, ?)", parameters);
                    System.out.println("cnt = " + cnt);
                    
                    jdbc.commitTransaction();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            jdbc.rollbackTransaction();
        }
        
        return Response.ok().build();
    }
}
