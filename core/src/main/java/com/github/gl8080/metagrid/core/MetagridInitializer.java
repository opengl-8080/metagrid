package com.github.gl8080.metagrid.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.github.gl8080.metagrid.core.config.ConfigInitializer;
import com.github.gl8080.metagrid.core.migration.MetagridMigration;

@WebListener
public class MetagridInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new ConfigInitializer().initialize();
            new MetagridMigration().migrate();
        } catch (Exception e) {
            throw new MetaGridException("metagrid の初期化に失敗しました。", e);
        }
    }

    @Override public void contextDestroyed(ServletContextEvent sce) {}
}
