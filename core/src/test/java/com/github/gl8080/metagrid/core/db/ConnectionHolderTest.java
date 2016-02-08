package com.github.gl8080.metagrid.core.db;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class ConnectionHolderTest {

    @Mocked
    private InitialContext context;
    @Mocked
    private DataSourceConfig config;
    @Mocked
    private DataSource ds;
    @Mocked
    private Connection con1;
    @Mocked
    private Connection con2;
    
    @Before
    public void setup() throws Exception {
        new NonStrictExpectations() {{
            config.getJndi(); result = "text.config.jndi";
            context.lookup("text.config.jndi"); result = ds;
            ds.getConnection(); returns(con1, con2);
        }};
    }
    
    @Test
    public void JNDIルックアップしたコネクションを取得できる() throws Exception {
        // exercise
        Connection con = ConnectionHolder.get(config);
        
        // verify
        assertThat(con).isSameAs(this.con1);
    }
    
    @Test
    public void _２回目も同じコネクションインスタンスを返す() throws Exception {
        // exercise
        ConnectionHolder.get(config);
        Connection con = ConnectionHolder.get(config);
        
        // verify
        assertThat(con).isSameAs(this.con1);
    }
    
    @Test
    public void スレッドが異なる場合は_それぞれで別々のコネクションが取得できる() throws Exception {
        // exercise
        ExecutorService service = Executors.newFixedThreadPool(2);
        
        Future<Connection> submit1 = service.submit(new TestThread(config));
        Future<Connection> submit2 = service.submit(new TestThread(config));
        
        // verify
        assertThat(Arrays.asList(submit1.get(), submit2.get())).containsOnly(con1, con2);
    }
    
    private static class TestThread implements Callable<Connection> {
        
        private DataSourceConfig config;
        
        public TestThread(DataSourceConfig config) {
            this.config = config;
        }

        @Override
        public Connection call() throws Exception {
            Thread.sleep(100);
            return ConnectionHolder.get(config);
        }
    }
}
