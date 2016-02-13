package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.DataSourceConfig;

public class ConnectionHolder {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHolder.class);
    
    private static final ThreadLocal<Map<String, Connection>> holder = new ThreadLocal<>();
    
    public synchronized static Connection get(DataSourceConfig config) {
        Objects.requireNonNull(config);
        Map<String, Connection> map = holder.get();
        
        if (map == null) {
            map = new HashMap<>();
        }
        
        String name = config.getName();
        
        if (!map.containsKey(name)) {
            Connection con = initConnection(config);
            map.put(name, con);
            holder.set(map);
            logger.debug("{} : get connection '{}'", Thread.currentThread().getName(), name);
        }
        
        return map.get(name);
    }
    
    public synchronized static void close() {
        Map<String, Connection> map = holder.get();
        
        if (map == null) {
            return;
        }
        
        Exception exception = null;
        
        for (Entry<String, Connection> entry : map.entrySet()) {
            try {
                entry.getValue().close();
                logger.debug("{} : close connection '{}'", Thread.currentThread().getName(), entry.getKey());
            } catch (Exception e) {
                exception = e;
                logger.error("コネクションのクローズでエラーが発生しました。", e);
            } finally {
                holder.remove();
                logger.debug("{} : remove connection '{}'", Thread.currentThread().getName(), entry.getKey());
            }
        }
        
        if (exception != null) {
            throw new MetaGridException(exception);
        }
    }
    
    private static Connection initConnection(DataSourceConfig config) {
        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup(config.getJndi());
            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            throw new MetaGridException(e);
        }
    }
}
