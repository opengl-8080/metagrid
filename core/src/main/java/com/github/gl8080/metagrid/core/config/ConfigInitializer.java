package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;

import com.github.gl8080.metagrid.core.infrastructure.task.AsyncTaskExecutor;
import com.github.gl8080.metagrid.core.util.message.ResourceBundleHelper;

public class ConfigInitializer {
    
    public void initialize() {
        InputStream in = MetagridConfig.getConfigFileStream();
        MetagridConfig.initialize(in);
        AsyncTaskExecutor.initialize();
        ResourceBundleHelper.initialize();
    }
}
