package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;

public class ConfigInitializer {
    
    public void initialize() {
        InputStream in = MetagridConfig.getConfigFileStream();
        MetagridConfig.initialize(in);
    }
}
