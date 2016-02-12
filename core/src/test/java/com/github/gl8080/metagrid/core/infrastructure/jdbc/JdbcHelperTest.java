package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

@RunWith(HierarchicalContextRunner.class)
public class JdbcHelperTest {
    
    private static final String SQL_TEST = "select * from test";
    
    public static class Base {
        @Mocked
        public DataSource ds;
        @Mocked
        public Connection con;
        @Mocked
        public ConnectionHolder holder;
        @Mocked
        public DataSourceConfig dsConfig;
        @Mocked
        public MetagridConfig config;
        @Mocked
        public PreparedStatement ps;
        @Mocked
        public ResultSet rs;
        @Mocked
        public ResultSetConverter<?> converter;
        
        public JdbcHelper helper;
        public int counter;
        public Sql sql;
        
        @Before
        public void setup() throws Exception {
            new NonStrictExpectations() {{
                MetagridConfig.getInstance().getDefaultDataSource(); result = dsConfig;
                ConnectionHolder.get(dsConfig); result = con;
                con.getMetaData().getDatabaseProductName(); result = "Oracle";
                ds.getConnection(); result = con;
                con.prepareStatement(SQL_TEST); result = ps;
                ps.executeQuery(); result = rs;
                
                rs.next(); returns(true, true, false);
            }};
            
            helper = new JdbcHelper();
            sql = new Sql(SQL_TEST);
        }
    }
    
    public class データベース種別の取得 extends Base {

        @Test
        public void データベースの種別を取得できる() throws Exception {
            // exercise
            DatabaseType databaseType = helper.getDatabaseType();
            
            // verify
            assertThat(databaseType).isEqualTo(DatabaseType.ORACLE);
        }
        
        @Test
        public void データソースから取得したコネクションの情報を元にデータベース種別を取得できる() throws Exception {
            // exercise
            DatabaseType databaseType = JdbcHelper.getDatabaseType(ds);
            
            // verify
            assertThat(databaseType).isEqualTo(DatabaseType.ORACLE);
        }
    }
    
    public class queryListメソッド extends Base {

        @Test
        public void 指定したSqlを実行して_コンバーターが変換した結果をListに詰めて返す() throws Exception {
            // exercise
            List<String> result = helper.queryList(sql, new ResultSetConverter<String>() {
                
                private String[] results = {"result1", "result2"};
                
                @Override
                public String convert(ResultSet actual) {
                    assertThat(actual).as("ResultSet").isSameAs(rs);
                    return results[counter++];
                }
            });
            
            // verify
            assertThat(result).as("result").containsExactly("result1", "result2");
            assertThat(counter).as("consume の呼び出し回数").isEqualTo(2);
        }
        
        @Test
        public void SQLに設定されたパラメータがPreparedStatementにセットされること() throws Exception {
            // setup
            sql.setParameters(1, "test", true);
            
            // exercise
            helper.queryList(sql, converter);
            
            // verify
            new Verifications() {{
                ps.setObject(1, 1);
                ps.setObject(2, "test");
                ps.setObject(3, true);
            }};
        }
    }
    
    public class queryメソッド extends Base {
        
        @Test
        public void 指定したSqlを実行して_コンバーターが変換した結果を返す() throws Exception {
            // exercise
            String result = helper.query(sql, new ResultSetConverter<String>() {

                @Override
                public String convert(ResultSet actual) {
                    assertThat(actual).as("ResultSet").isSameAs(rs);
                    counter++;
                    return "RESULT";
                }
            });
            
            // verify
            assertThat(result).as("getResult").isEqualTo("RESULT");
            assertThat(counter).as("consume の呼び出し回数").isEqualTo(1);
        }
        
        @Test
        public void SQLに設定されたパラメータがPreparedStatementにセットされること() throws Exception {
            // setup
            sql.setParameters(1, "test", true);
            
            // exercise
            helper.query(sql, converter);
            
            // verify
            new Verifications() {{
                ps.setObject(1, 1);
                ps.setObject(2, "test");
                ps.setObject(3, true);
            }};
        }
    }
    
    public class 単一検索 extends Base {
        
        @Test
        public void 検索結果の１件目のデータがそのまま返される() throws Exception {
            // setup
            new NonStrictExpectations() {{
                rs.getObject(1); result = 999;
            }};
            
            // exercise
            int actual = helper.querySingle(sql);
            
            assertThat(actual).isEqualTo(999);
        }
    }
    
    public class updateメソッド extends Base {

        @Test
        public void 指定したSQLでupdateQueryが実行される() throws Exception {
            // exercise
            helper.update(sql);
            
            // verify
            new Verifications() {{
                ps.executeUpdate();
            }};
        }
        
        @Test
        public void updateQueryの実行結果がそのままreturnされる() throws Exception {
            // setup
            new NonStrictExpectations() {{
                ps.executeUpdate(); result = 3;
            }};
            
            // exercise
            int actual = helper.update(sql);
            
            // verify
            assertThat(actual).isEqualTo(3);
        }
        
        @Test
        public void SQLに設定されたパラメータがPreparedStatementにセットされること() throws Exception {
            // setup
            sql.setParameters(1, "test", true);
            
            // exercise
            helper.update(sql);
            
            // verify
            new Verifications() {{
                ps.setObject(1, 1);
                ps.setObject(2, "test");
                ps.setObject(3, true);
            }};
        }
    }
}
