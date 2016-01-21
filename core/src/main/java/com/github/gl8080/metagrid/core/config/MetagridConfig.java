package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metagrid")
public class MetagridConfig {
    
    private static final String CONFIG_FILE_PATH = "/metagrid.xml";
    
    private static MetagridConfig instance;
    
    public static InputStream getConfigFileStream() {
        return MetagridConfig.class.getResourceAsStream(CONFIG_FILE_PATH);
    }

    public static void initialize(InputStream in) {
        instance = JAXB.unmarshal(in, MetagridConfig.class);
    }

    public static MetagridConfig getInstance() {
        return instance;
    }
    
    private DataSourceConfig datasource;

    @XmlElement
    public DataSourceConfig getDatasource() {
        return datasource;
    }

    void setDatasource(DataSourceConfig datasource) {
        this.datasource = datasource;
    }
}
