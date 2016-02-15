package com.github.gl8080.metagrid.core.infrastructure.upload;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.domain.upload.UploadFile;
import com.github.gl8080.metagrid.core.domain.upload.UploadFileRepository;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.UpdateResult;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.AutoGenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategyFactory;
import com.github.gl8080.metagrid.core.util.SqlResolver;

public class UploadFileRepositoryImpl implements UploadFileRepository {

    @Override
    public int register(UploadFile uploadFile) {
        DataSourceConfig repository = MetagridConfig.getInstance().getRepositoryDataSource();
        JdbcHelper jdbc = new JdbcHelper(repository);
        
        jdbc.beginTransaction();
        GenerateIdStrategy strategy = this.createGenerateIdStrategy(jdbc);
        
        Sql sql = new SqlResolver().resolve(jdbc.getDatabaseType(), UploadFileRepository.class, "register");
        
        sql.setParameters(
            uploadFile.getName(),
            uploadFile.getFile(),
            uploadFile.getRecordCount().getTotalCount(),
            uploadFile.getRecordCount().getProcessedCount(),
            uploadFile.getProcessingTime().getTime(),
            uploadFile.getStatus().name()
        );
        
        UpdateResult updateResult = jdbc.update(sql, strategy);
        uploadFile.setId(updateResult.getGeneratedId());
        
        jdbc.commitTransaction();
        
        return updateResult.getUpdateCount();
    }
    
    private GenerateIdStrategy createGenerateIdStrategy(JdbcHelper jdbc) {
        if (jdbc.is(DatabaseType.ORACLE)) {
            return GenerateIdStrategyFactory.sequence("UPLOAD_FILE_SEQ", jdbc);
        } else {
            return new AutoGenerateIdStrategy();
        }
    }
}
