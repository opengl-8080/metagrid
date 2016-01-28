package com.github.gl8080.metagrid.core.migration;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.db.DatabaseType;
import com.github.gl8080.metagrid.core.db.JdbcHelper;

public class MetagridMigration {
    private static final Logger logger = LoggerFactory.getLogger(MetagridMigration.class);

    public void migrate() throws NamingException {
        DataSourceConfig repositoryConfig = MetagridConfig.getInstance().getRepositoryDataSource();
        JdbcHelper jdbc = new JdbcHelper(repositoryConfig);
        
        DataSource repository = jdbc.getDataSource();
        
        Flyway flyway = new Flyway();
        flyway.setDataSource(repository);
        
        DatabaseType databaseType = jdbc.getDatabaseType();
        logger.info("データベース種別 = {}", databaseType);
        flyway.setLocations("db/migration/" + databaseType.name().toLowerCase());
        
        flyway.clean(); // TODO 開発が終了したら除去
        
        logger.info("リポジトリをマイグレーションしています。");
        flyway.migrate();
    }
}
