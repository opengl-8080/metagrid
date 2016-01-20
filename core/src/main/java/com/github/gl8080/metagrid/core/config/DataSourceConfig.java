package com.github.gl8080.metagrid.core.config;

import javax.xml.bind.annotation.XmlAttribute;

public class DataSourceConfig {
    
    private String jndi;

    @XmlAttribute
    public String getJndi() {
        return jndi;
    }

    void setJndi(String jndi) {
        this.jndi = jndi;
    }
    
}
