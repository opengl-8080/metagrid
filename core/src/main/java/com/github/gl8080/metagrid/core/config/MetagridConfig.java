package com.github.gl8080.metagrid.core.config;

import java.io.Reader;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metagrid")
public class MetagridConfig {

    public static MetagridConfig read(Reader reader) {
        return JAXB.unmarshal(reader, MetagridConfig.class);
    }
    
    
    public DataSourceConfig datasource;

}
