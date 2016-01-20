package com.github.gl8080.metagrid.core.config;

import static org.assertj.core.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class MetagridConfigTest {

    @Test
    public void ルートタグは_metagrid_でマッピングされること() {
        // setup
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                   + "<metagrid>"
                   + "</metagrid>";
        
        // exercise
        MetagridConfig config = parseXml(xml);
        
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
        MetagridConfig config = parseXml(xml);
        
        // verify
        assertThat(config.datasource.jndi).isEqualTo("foo:bar/datasource");
    }

    private MetagridConfig parseXml(String xml) {
        Reader reader = new StringReader(xml);
        return MetagridConfig.read(reader);
    }
}
