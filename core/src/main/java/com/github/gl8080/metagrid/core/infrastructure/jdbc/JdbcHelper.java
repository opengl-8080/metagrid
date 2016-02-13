package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public <T> T query(Sql sql, ResultSetConverter<T> converter) {
        try (PreparedStatement ps = this.con.prepareStatement(sql.getText());) {
            this.setupParameter(sql, ps);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converter.convert(rs);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }

    public <T> List<T> queryList(Sql sql, ResultSetConverter<T> converter) {
        try (PreparedStatement ps = this.con.prepareStatement(sql.getText());) {
            this.setupParameter(sql, ps);
            
            try (ResultSet rs = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                
                while (rs.next()) {
                    list.add(converter.convert(rs));
                }
                
                return list;
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T querySingle(Sql sql) {
        try (PreparedStatement ps = this.con.prepareStatement(sql.getText());) {
            this.setupParameter(sql, ps);
            
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return (T)rs.getObject(1);
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }

    public int update(Sql sql) {
        try (PreparedStatement ps = this.con.prepareStatement(sql.getText());) {
            this.setupParameter(sql, ps);
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new MetaGridException(e);
        }
    }

    private void setupParameter(Sql sql, PreparedStatement ps) throws SQLException {
        int index = 1;
        for (Object parameter : sql.getParameters()) {
            ps.setObject(index++, parameter);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("DataSource{name={}, jndi={}}", config.getName(), config.getJndi());
            logger.debug("SQL : \n{}", sql.getText());
            logger.debug("Parameters : {}", Arrays.toString(sql.getParameters()));
        }
    }

    public List<Map<String, Object>> queryListMap(Sql sql) {
        return this.queryList(sql, new ResultSetConverter<Map<String, Object>>() {

            @Override
            public Map<String, Object> convert(ResultSet rs) throws SQLException {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                Map<String, Object> map = new HashMap<>();
                
                for (int i=0; i<columnCount; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    map.put(columnName, rs.getObject(columnName));
                }
                
                return map;
            }
        });
    }
}
