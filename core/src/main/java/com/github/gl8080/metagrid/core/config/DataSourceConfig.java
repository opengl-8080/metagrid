package com.github.gl8080.metagrid.core.config;

import javax.xml.bind.annotation.XmlAttribute;

public class DataSourceConfig {

    @XmlAttribute
    private String jndi;
    @XmlAttribute(name="default")
    private boolean isDefault;

    public String getJndi() {
        return jndi;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
