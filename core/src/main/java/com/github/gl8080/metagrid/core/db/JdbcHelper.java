package com.github.gl8080.metagrid.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class JdbcHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
    
    private final DataSourceConfig config;
    
    public JdbcHelper() {
        this(MetagridConfig.getInstance().getDefaultDataSource());
    }
    
    public JdbcHelper(DataSourceConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
    }
    
    public DataSource getDataSource() throws NamingException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(this.config.getJndi());
        return ds;
    }
    
    public void executeQuery(String sql, ThrowableConsumer<ResultSet> consumer) {
        logger.debug(sql);
        
        try (Connection con = this.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            
            while (rs.next()) {
                consumer.consume(rs);
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public DatabaseType getDatabaseType() {
        try (Connection con = this.getDataSource().getConnection();) {
            String productName = con.getMetaData().getDatabaseProductName();
            return DatabaseType.of(productName);
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
}
