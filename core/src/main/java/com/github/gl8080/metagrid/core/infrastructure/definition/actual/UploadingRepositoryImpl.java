package com.github.gl8080.metagrid.core.infrastructure.definition.actual;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.upload.Uploading;
import com.github.gl8080.metagrid.core.domain.upload.UploadingRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.OracleSequenceStrategy;

public class UploadingRepositoryImpl implements UploadingRepository {

    @Override
    public int register(Uploading uploading) {
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
            uploading.getUploadFile().getName(),
            uploading.getUploadFile().getFile(),
            uploading.getRecordCount().getTotalCount(),
            uploading.getRecordCount().getProcessedCount(),
            uploading.getProcessingTime().getTime(),
            uploading.getStatus().name()
        );
        
        int updateCount = jdbc.update(sql);
        
        jdbc.commitTransaction();
        
        return updateCount;
    }
}
