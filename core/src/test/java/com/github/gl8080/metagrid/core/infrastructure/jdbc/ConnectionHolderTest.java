package com.github.gl8080.metagrid.core.infrastructure.jdbc;

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
import com.github.gl8080.metagrid.core.infrastructure.jdbc.ConnectionHolder;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class ConnectionHolderTest {

    private static final String JNDI_1 = "text.config.jndi1";
    private static final String JNDI_2 = "text.config.jndi2";
    private static final String DS_NAME_1 = "ds1";
    private static final String DS_NAME_2 = "ds2";
    
    @Mocked
    private InitialContext context;
    @Mocked
    private DataSourceConfig config1;
    @Mocked
    private DataSourceConfig config2;
    @Mocked
    private DataSource ds1;
    @Mocked
    private DataSource ds2;
    @Mocked
    private Connection con1;
    @Mocked
    private Connection con2;
    @Mocked
    private Connection con3;
    
    @Before
    public void setup() throws Exception {
        new NonStrictExpectations() {{
            config1.getJndi(); result = JNDI_1;
            config1.getName(); result = DS_NAME_1;
            context.lookup(JNDI_1); result = ds1;
            ds1.getConnection(); returns(con1, con2);
            
            config2.getJndi(); result = JNDI_2;
            config2.getName(); result = DS_NAME_2;
            context.lookup(JNDI_2); result = ds2;
            ds2.getConnection(); result = con3;
        }};
    }
    
    @Test
    public void 異なるコンフィグを渡したら_それに応じたコネクションが習得できる() throws Exception {
        // exercise
        ConnectionHolder.get(config1);
        Connection con = ConnectionHolder.get(config2);
        
        // verify
        assertThat(con).isSameAs(this.con3);
    }
    
    @Test
    public void JNDIルックアップしたコネクションを取得できる() throws Exception {
        // exercise
        Connection con = ConnectionHolder.get(config1);
        
        // verify
        assertThat(con).isSameAs(this.con1);
    }
    
    @Test
    public void _２回目も同じコネクションインスタンスを返す() throws Exception {
        // exercise
        ConnectionHolder.get(config1);
        Connection con = ConnectionHolder.get(config1);
        
        // verify
        assertThat(con).isSameAs(this.con1);
    }
    
    @Test
    public void スレッドが異なる場合は_それぞれで別々のコネクションが取得できる() throws Exception {
        // exercise
        ExecutorService service = Executors.newFixedThreadPool(2);
        
        Future<Connection> submit1 = service.submit(new TestThread(config1));
        Future<Connection> submit2 = service.submit(new TestThread(config1));
        
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
