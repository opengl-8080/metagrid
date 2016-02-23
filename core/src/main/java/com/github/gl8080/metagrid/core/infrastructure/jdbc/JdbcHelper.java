package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.AutoGenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.SequenceIdStrategy;

public class JdbcHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
    
    private final DataSourceConfig config;
    private final Connection con;
    private boolean inTransaction;

    public static JdbcHelper getRepositoryHelper() {
        return new JdbcHelper(MetagridConfig.getInstance().getRepositoryDataSource());
    }
    
    public JdbcHelper() {
        this(MetagridConfig.getInstance().getDefaultDataSource());
    }
    
    public JdbcHelper(DataSourceConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
        this.con = ConnectionHolder.get(this.config);
    }

    public boolean is(DatabaseType databaseType) {
        return this.getDatabaseType().equals(databaseType);
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
        if (this.inTransaction) {
            return;
        }
        
        try {
            this.con.setAutoCommit(false);
            this.inTransaction = true;
        } catch (SQLException e) {
            throw new MetaGridException(e);
        }
    }
    
    public void commitTransaction() {
        if (!this.inTransaction) {
            return;
        }
        
        try {
            this.con.commit();
            this.inTransaction = false;
        } catch (SQLException e) {
            throw new MetaGridException(e);
        }
    }
    
    public void rollbackTransaction() {
        if (!this.inTransaction) {
            return;
        }
        
        try {
            this.con.rollback();
            this.inTransaction = false;
        } catch (SQLException e) {
            throw new MetaGridException(e);
        }
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
    
    public UpdateResult update(Sql sql, GenerateIdStrategy strategy) {
        try (PreparedStatement ps = this.prepareStatement(sql, strategy)) {
            Long id = null;
            
            if (strategy instanceof SequenceIdStrategy) {
                id = strategy.generate();
                sql.prependParameter(id);
            }
            
            this.setupParameter(sql, ps);
            
            int updateCount = ps.executeUpdate();
            
            UpdateResult result = new UpdateResult();
            result.setUpdateCount(updateCount);

            if (strategy instanceof AutoGenerateIdStrategy) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    generatedKeys.next();
                    id = generatedKeys.getLong(1);
                }
            }
            
            result.setGeneratedId(id);
            
            return result;
        } catch (SQLException e) {
            throw new MetaGridException(e);
        }
    }

    private PreparedStatement prepareStatement(Sql sql, GenerateIdStrategy strategy) throws SQLException {
        if (strategy instanceof AutoGenerateIdStrategy) {
            logger.debug("prepareStatement with Statement.RETURN_GENERATED_KEYS");
            return this.con.prepareStatement(sql.getText(), Statement.RETURN_GENERATED_KEYS);
        } else {
            logger.debug("prepareStatement");
            return this.con.prepareStatement(sql.getText());
        }
    }

    private void setupParameter(Sql sql, PreparedStatement ps) throws SQLException {
        int index = 1;
        for (Object parameter : sql.getParameterList()) {
            
            if (parameter instanceof File) {
                File file = (File)parameter;
                
                try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                    ps.setCharacterStream(index, br);
                } catch (IOException e) {
                    throw new MetaGridException(e);
                }
            } else {
                ps.setObject(index, parameter);
            }
            
            index++;
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("DataSource{name={}, jndi={}}", config.getName(), config.getJndi());
            logger.debug("SQL : \n{}", sql.getText());
            logger.debug("Parameters : {}", sql.getParameterList());
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
