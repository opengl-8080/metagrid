package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.config.DataSourceConfig;
import com.github.gl8080.metagrid.core.config.MetagridConfig;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.AutoGenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.GenerateIdStrategy;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.id.SequenceIdStrategy;
import com.github.gl8080.metagrid.core.util.Maps;

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
        public PreparedStatement genPs;
        @Mocked
        public ResultSet rs;
        @Mocked
        public ResultSet generatedKeysRs;
        @Mocked
        public ResultSetConverter<?> converter;
        @Mocked
        public ResultSetMetaData rsMetaData;
        
        public static final String COLUMN_NAME_1 = "column1";
        public static final String COLUMN_NAME_2 = "column2";
        public static final String COLUMN_NAME_3 = "column3";
        
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
        
        @Test
        public void 簡潔な方法_一致している場合() {
            // exercise
            boolean actual = helper.is(DatabaseType.ORACLE);
            
            // verify
            assertThat(actual).isTrue();
        }
        
        @Test
        public void 簡潔な方法_一致しない場合() {
            // exercise
            boolean actual = helper.is(DatabaseType.MYSQL);
            
            // verify
            assertThat(actual).isFalse();
        }
    }
    
    public class queryMapメソッド extends Base {
        
        @SuppressWarnings("unchecked")
        @Test
        public void 指定したSQLを実行して_結果をキーをカラム名にしたMapに詰めて返す() throws Exception {
            new NonStrictExpectations() {{
                rs.getMetaData(); result = rsMetaData;
                rsMetaData.getColumnCount(); result = 3;
                rsMetaData.getColumnName(1); result = COLUMN_NAME_1;
                rsMetaData.getColumnName(2); result = COLUMN_NAME_2;
                rsMetaData.getColumnName(3); result = COLUMN_NAME_3;
                
                rs.getObject(COLUMN_NAME_1); returns("hoge", "fuga", "piyo");
                rs.getObject(COLUMN_NAME_2); returns(true, false, true);
                rs.getObject(COLUMN_NAME_3); returns(1, 2, 3);
                
                rs.next(); returns(true, true, true, false);
            }};
            
            List<Map<String, Object>> result = helper.queryListMap(sql);
            
            assertThat(result).containsExactly(
                Maps.<String, Object>hashMap(COLUMN_NAME_1, "hoge").entry(COLUMN_NAME_2, true).entry(COLUMN_NAME_3, 1),
                Maps.<String, Object>hashMap(COLUMN_NAME_1, "fuga").entry(COLUMN_NAME_2, false).entry(COLUMN_NAME_3, 2),
                Maps.<String, Object>hashMap(COLUMN_NAME_1, "piyo").entry(COLUMN_NAME_2, true).entry(COLUMN_NAME_3, 3)
            );
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
    
//    public class updateメソッド extends Base {
//
//        @Test
//        public void 指定したSQLでupdateQueryが実行される() throws Exception {
//            // exercise
//            helper.update(sql);
//            
//            // verify
//            new Verifications() {{
//                ps.executeUpdate();
//            }};
//        }
//        
//        @Test
//        public void 更新件数とDBによって自動生成された情報がreturnされること() throws Exception {
//            // setup
//            new NonStrictExpectations() {{
//                ps.executeUpdate(); result = 3;
//                ps.getGeneratedKeys(); result = generatedKeysRs;
//                generatedKeysRs.next(); result = true;
//                generatedKeysRs.getLong(1); result = 123L;
//            }};
//            
//            // exercise
//            UpdateResult actual = helper.updateWithAutoGenerated(sql);
//            
//            // verify
//            assertThat(actual.getUpdateCount()).as("更新件数").isEqualTo(3);
//            assertThat(actual.getGeneratedId()).as("生成されたID").isEqualTo(123);
//        }
//        
//        @Test
//        public void SQLに設定されたパラメータがPreparedStatementにセットされること() throws Exception {
//            // setup
//            sql.setParameters(1, "test", true);
//            
//            // exercise
//            helper.update(sql);
//            
//            // verify
//            new Verifications() {{
//                ps.setObject(1, 1);
//                ps.setObject(2, "test");
//                ps.setObject(3, true);
//            }};
//        }
//    }
    
    public class ID生成指定のupdate {
        
        public class ID生成が自動生成の場合 extends Base {
            
            private GenerateIdStrategy strategy = new AutoGenerateIdStrategy();
            
            @Test
            public void パラメータはそのまま使用される() throws Exception {
                // setup
                sql.setParameters(1, "test", true);

                // exercise
                helper.update(sql, strategy);
                
                // verify
                new Verifications() {{
                    genPs.setObject(1, 1);
                    genPs.setObject(2, "test");
                    genPs.setObject(3, true);
                }};
            }
            
            @Test
            public void 更新件数とDBによって自動生成された情報がreturnされること() throws Exception {
                // setup
                new NonStrictExpectations() {{
                    con.prepareStatement(SQL_TEST, Statement.RETURN_GENERATED_KEYS); result = genPs;
                    genPs.getGeneratedKeys(); result = generatedKeysRs;
                    genPs.executeUpdate(); result = 3;
                    generatedKeysRs.next(); result = true;
                    generatedKeysRs.getLong(1); result = 123L;
                }};
                
                // exercise
                UpdateResult actual = helper.update(sql, strategy);
                
                // verify
                assertThat(actual.getUpdateCount()).as("更新件数").isEqualTo(3);
                assertThat(actual.getGeneratedId()).as("生成されたID").isEqualTo(123);
            }
        }
        
        public class ID生成がシーケンス生成の場合 extends Base {
            
            @Mocked
            private SequenceIdStrategy strategy;
            
            @Before
            public void setupStrategy() {
                new NonStrictExpectations() {{
                    strategy.generate(); result = 12L;
                }};
            }
            
            @Test
            public void ID生成が返した値がパラメータの先頭に追加されて使用されること() throws Exception {
                // setup
                sql.setParameters(1, "test", true);

                // exercise
                helper.update(sql, strategy);
                
                // verify
                new Verifications() {{
                    ps.setObject(1, 12L);
                    ps.setObject(2, 1);
                    ps.setObject(3, "test");
                    ps.setObject(4, true);
                }};
            }
            
            @Test
            public void 更新件数とID生成によって生成されたID情報がreturnされること() throws Exception {
                // setup
                new NonStrictExpectations() {{
                    ps.executeUpdate(); result = 2;
                }};
                
                // exercise
                UpdateResult actual = helper.update(sql, strategy);
                
                // verify
                assertThat(actual.getUpdateCount()).as("更新件数").isEqualTo(2);
                assertThat(actual.getGeneratedId()).as("生成されたID").isEqualTo(12L);
            }
        }
    }
}
