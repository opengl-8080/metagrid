package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="metagrid")
public class MetagridConfig {
    private static final Logger logger = LoggerFactory.getLogger(MetagridConfig.class);
    
    private static final String CONFIG_FILE_PATH = "/metagrid.xml";
    
    private static MetagridConfig instance;
    
    public static InputStream getConfigFileStream() {
        InputStream in = MetagridConfig.class.getResourceAsStream(CONFIG_FILE_PATH);
        
        if (in == null) {
            throw new IllegalStateException("metagrid.xml がクラスパス直下に存在しません。");
        }
        
        return in;
    }

    public static void initialize(InputStream in) {
        instance = JAXB.unmarshal(in, MetagridConfig.class);
        logger.info("Complete to load configuration file.");
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
