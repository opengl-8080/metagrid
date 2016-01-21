package com.github.gl8080.metagrid.core.config;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class MetagridConfigTest {
    
    
    
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
                   + "  <datasource jndi=\"foo:bar/datasource\" />"
                   + "</metagrid>";
        
        // exercise
        MetagridConfig config = loadConfig(xml);
        
        // verify
        assertThat(config.getDatasource().getJndi()).isEqualTo("foo:bar/datasource");
    }
    
    private MetagridConfig loadConfig(String xml) {
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        MetagridConfig.initialize(in);
        return MetagridConfig.getInstance();
    }
}
