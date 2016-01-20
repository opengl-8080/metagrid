package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metagrid")
public class MetagridConfig {
    
    public static final MetagridConfig instance;
    
    static {
        InputStream resourceAsStream = MetagridConfig.class.getResourceAsStream("/metagrid.xml");
        if (resourceAsStream == null) {
            throw new IllegalStateException("metagrid.xml がクラスパス直下に存在しません。");
        }
        instance = read(resourceAsStream);
    }
    
    static MetagridConfig read(InputStream input) {
        return JAXB.unmarshal(input, MetagridConfig.class);
    }
    
    static MetagridConfig read(Reader reader) {
        return JAXB.unmarshal(reader, MetagridConfig.class);
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
