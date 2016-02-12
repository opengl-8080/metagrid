package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Objects;

import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jndi.JndiHelper;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class JdbcHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
    
    private final DataSourceConfig config;
    private final Connection con;
    
    public JdbcHelper() {
        this(MetagridConfig.getInstance().getDefaultDataSource());
    }
    
    public JdbcHelper(DataSourceConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
        this.con = ConnectionHolder.get(this.config);
    }
    
    public int queryInt(String sql) {
        logger.debug(sql);
        
        try (PreparedStatement ps = this.con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();) {
            
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public void query(String sql, ThrowableConsumer<ResultSet> consumer) {
        logger.debug(sql);
        
        try (PreparedStatement ps = this.con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();) {
            
            while (rs.next()) {
                consumer.consume(rs);
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public int update(String sql, Object[] parameters) {
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
            logger.debug(Arrays.toString(parameters));
        }
        
        try (PreparedStatement ps = this.con.prepareStatement(sql);) {
            
            int idx = 0;
            for (Object parameter : parameters) {
                ps.setObject(++idx, parameter);
            }
            
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public DatabaseType getDatabaseType() {
        try {
            String productName = this.con.getMetaData().getDatabaseProductName();
            return DatabaseType.of(productName);
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public static DatabaseType getDatabaseType(DataSource ds) {
        try (Connection con = ds.getConnection()) {
            String productName = con.getMetaData().getDatabaseProductName();
            return DatabaseType.of(productName);
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
    
    public void beginTransaction() {
        try {
            this.getUserTransaction().begin();
        } catch (NotSupportedException | SystemException e) {
            throw new MetaGridException(e);
        }
    }
    
    public void commitTransaction() {
        try {
            this.getUserTransaction().commit();
        } catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            throw new MetaGridException(e);
        }
    }
    
    public void rollbackTransaction() {
        try {
            this.getUserTransaction().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            throw new MetaGridException(e);
        }
    }
    
    private UserTransaction getUserTransaction() {
        return new JndiHelper().lookup("java:comp/UserTransaction");
    }
}
