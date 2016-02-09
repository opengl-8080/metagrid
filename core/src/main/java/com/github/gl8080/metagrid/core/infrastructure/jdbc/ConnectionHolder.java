package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
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
    
    private static final ThreadLocal<Connection> holder = new ThreadLocal<>();
    
    public synchronized static Connection get(DataSourceConfig config) {
        Objects.requireNonNull(config);
        Connection con = holder.get();
        
        if (con == null) {
            con = initConnection(config);
            holder.set(con);
            logger.debug(Thread.currentThread().getName() + " : get connection");
        }
        
        return con;
    }
    
    public synchronized static void close() {
        Connection con = holder.get();
        
        if (con != null) {
            try {
                con.close();
                logger.debug(Thread.currentThread().getName() + " : close connection");
            } catch (SQLException e) {
                throw new MetaGridException(e);
            } finally {
                holder.remove();
                logger.debug(Thread.currentThread().getName() + " : remove connection");
            }
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
