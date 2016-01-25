package com.github.gl8080.metagrid.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.util.Consumer;

public class JdbcHelper {
    
    public DataSource getDataSource() throws NamingException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(MetagridConfig.getInstance().getDatasource().getJndi());
        return ds;
    }
    
    public void executeQuery(String sql, Consumer<ResultSet> consumer) {
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
}
