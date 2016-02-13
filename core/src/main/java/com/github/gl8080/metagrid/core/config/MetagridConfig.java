package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
        instance.repository.setName("repository");
        instance.datasources.get(0).setName("default");
        
        logger.info("Complete to load configuration file.");
    }

    public static MetagridConfig getInstance() {
        return instance;
    }

    @XmlElement(name="datasource")
    @XmlElementWrapper(name="datasources")
    private List<DataSourceConfig> datasources;
    
    @XmlElement(name="repository")
    private DataSourceConfig repository;

    public List<DataSourceConfig> getDataSources() {
        return this.datasources;
    }
    
    public DataSourceConfig getDefaultDataSource() {
        return this.datasources.get(0);
    }

    public DataSourceConfig getRepositoryDataSource() {
        return this.repository;
    }
}
