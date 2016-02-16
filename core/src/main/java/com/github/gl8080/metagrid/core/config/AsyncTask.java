package com.github.gl8080.metagrid.core.config;

import javax.xml.bind.annotation.XmlAttribute;

public class AsyncTask {
    
    @XmlAttribute
    private int max = 5;

    public int getMax() {
        return this.max;
    }
}
