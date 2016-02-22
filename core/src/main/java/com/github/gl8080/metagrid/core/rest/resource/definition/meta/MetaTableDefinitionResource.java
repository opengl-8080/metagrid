package com.github.gl8080.metagrid.core.rest.resource.definition.meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.upload.RecordCount;
import com.github.gl8080.metagrid.core.upload.UploadFile;
import com.github.gl8080.metagrid.core.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.upload.csv.CsvRecordParser;
import com.github.gl8080.metagrid.core.upload.csv.CsvRecordProcessor;
import com.github.gl8080.metagrid.core.util.ComponentLoader;

@Path("meta-table-definition")
public class MetaTableDefinitionResource {
    
    @PUT
    @Consumes("text/csv")
    public Response load(InputStream in, @HeaderParam("File-Name") String fileName) throws IOException {

        File tmp = File.createTempFile("meta_table_def_", ".csv");
        
        UploadFile uploadFile = new UploadFile(fileName, tmp);
        
        try {
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            try (BufferedReader br = Files.newBufferedReader(tmp.toPath(), StandardCharsets.UTF_8)) {
                int totalCount = 0;
                
                while (br.readLine() != null) {
                    totalCount++;
                }
                
                RecordCount recordCount = new RecordCount(totalCount);
                uploadFile.setRecordCount(recordCount);
            }
            
            JdbcHelper repository = new JdbcHelper(MetagridConfig.getInstance().getRepositoryDataSource());
            try {
                repository.beginTransaction();
                ComponentLoader.getComponent(UploadFileRepository.class).register(uploadFile);
                repository.commitTransaction();
            } catch (Exception e) {
                repository.rollbackTransaction();
                throw new MetaGridException(e);
            }
            
            CsvRecordProcessor processor = new CsvRecordProcessor() {
                
                @Override
                public void process(List<String> record) {
                    // TODO 自動生成されたメソッド・スタブ
                    
                }
            };
            CsvRecordParser parser = new CsvRecordParser(processor);
            
            uploadFile.eachLine(parser);
        } finally {
            tmp.delete();
            System.out.println("delete file");
        }
//        
//        CsvUploadFile csv = new CsvUploadFile(in);
//        
//        MetaTableDefinitionLoadService service = new MetaTableDefinitionLoadService();
//        
//        csv.each(service);
        
        return Response.ok().build();
    }
}
