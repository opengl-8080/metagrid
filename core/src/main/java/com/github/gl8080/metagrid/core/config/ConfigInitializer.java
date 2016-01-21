package com.github.gl8080.metagrid.core.config;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConfigInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        InputStream in = MetagridConfig.getConfigFileStream();
        MetagridConfig.initialize(in);
    }

    @Override public void contextDestroyed(ServletContextEvent sce) {}
}
