package com.github.gl8080.metagrid.core.config;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class MetagridConfigTest {
    
    @Rule
    public ExpectedException ex = ExpectedException.none();
    
    public static class Base {
        
    }
    
    public class マッピングのテスト extends Base {

        @Test
        public void 設定ファイルがクラスパス直下に存在しない場合は例外をスローする() {
            // verify
            ex.expect(IllegalStateException.class);
            ex.expectMessage("metagrid.xml がクラスパス直下に存在しません。");
            
            // exercise
            MetagridConfig.getConfigFileStream();
        }
        
        @Test
        public void ルートタグは_metagrid_でマッピングされること() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            assertThat(config).isNotNull();
        }

        @Test
        public void データソースのJNDIをマッピングできること() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "  <datasources>"
                       + "    <datasource jndi=\"foo:bar/datasource1\" />"
                       + "    <datasource jndi=\"foo:bar/datasource2\" />"
                       + "    <datasource jndi=\"foo:bar/datasource3\" />"
                       + "  </datasources>"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            List<DataSourceConfig> datasources = config.getDataSources();
            
            assertThat(datasources)
                .extracting("jndi")
                .contains("foo:bar/datasource1",
                          "foo:bar/datasource2",
                          "foo:bar/datasource3");
        }

        @Test
        public void データソースのdefaultをマッピングできる() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "  <datasources>"
                       + "    <datasource jndi=\"foo:bar/datasource1\" default=\"true\" />"
                       + "  </datasources>"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            List<DataSourceConfig> datasources = config.getDataSources();
            
            assertThat(datasources)
                .extracting("default")
                .contains(true);
        }

        @Test
        public void リポジトリのJNDIをマッピングできる() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "  <repository jndi=\"fizz:bazz/datasource\" />"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            DataSourceConfig datasource = config.getRepositoryDataSource();
            
            assertThat(datasource.getJndi()).isEqualTo("fizz:bazz/datasource");
        }

        @Test
        public void 非同期タスクの同時実行数を指定できる() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "  <asyncTask max=\"4\" />"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            AsyncTask asyncTask = config.getAsyncTask();
            
            assertThat(asyncTask.getMax()).isEqualTo(4);
        }

        @Test
        public void 非同期タスクの同時実行数が指定されていない場合は_デフォルトで5() {
            // setup
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                       + "<metagrid>"
                       + "</metagrid>";
            
            // exercise
            MetagridConfig config = loadConfig(xml);
            
            // verify
            AsyncTask asyncTask = config.getAsyncTask();
            
            assertThat(asyncTask.getMax()).isEqualTo(5);
        }
    }
    
    private MetagridConfig loadConfig(String xml) {
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        MetagridConfig.initialize(in);
        return MetagridConfig.getInstance();
    }
}
