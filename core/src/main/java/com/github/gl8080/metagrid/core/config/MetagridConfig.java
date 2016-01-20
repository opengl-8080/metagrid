package com.github.gl8080.metagrid.core.config;

import java.io.Reader;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metagrid")
public class MetagridConfig {
    
    public static MetagridConfig read(Reader reader) {
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
