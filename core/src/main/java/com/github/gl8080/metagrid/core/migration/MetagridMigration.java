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
        
        switch (databaseType) {
        case ORACLE:
            flyway.setLocations("db/migration/oracle");
            break;
        case MYSQL:
            flyway.setLocations("db/migration/mysql");
            break;
        case POSTGRESQL:
            flyway.setLocations("db/migration/postgresql");
            break;
        default:
            throw new RuntimeException("不明なデータベース種別 : " + databaseType);
        }
        
        logger.info("リポジトリをマイグレーションしています。");
        flyway.migrate();
    }
}
