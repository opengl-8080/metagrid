package com.github.gl8080.metagrid.core.infrastructure.definition.actual;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.OracleSequenceStrategy;

public class UploadFileRepositoryImpl implements UploadFileRepository {

    @Override
    public int register(UploadFile uploadFile) {
        DataSourceConfig repository = MetagridConfig.getInstance().getRepositoryDataSource();
        JdbcHelper jdbc = new JdbcHelper(repository);
        
        jdbc.beginTransaction();
        GenerateIdStrategy strategy = new OracleSequenceStrategy("UPLOAD_FILE_SEQ", jdbc);
        long id = strategy.generate();
        
        Sql sql = new Sql("INSERT INTO UPLOAD_FILE (ID, FILE_NAME, CONTENTS, TOTAL_RECORD_COUNT, "
                + "PROCESSED_RECORD_COUNT, TOTAL_PASSED_TIME, STATUS) VALUES ("
                + "?, ?, ?, ?, ?, ?, ?)");
        
        sql.setParameters(
            id,
            uploadFile.getName(),
            uploadFile.getFile(),
            uploadFile.getRecordCount().getTotalCount(),
            uploadFile.getRecordCount().getProcessedCount(),
            uploadFile.getProcessingTime().getTime(),
            uploadFile.getStatus().name()
        );
        
        int updateCount = jdbc.update(sql);
        
        jdbc.commitTransaction();
        
        return updateCount;
    }
}
