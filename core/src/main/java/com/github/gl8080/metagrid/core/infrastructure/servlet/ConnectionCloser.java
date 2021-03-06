package com.github.gl8080.metagrid.core.infrastructure.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.ConnectionHolder;

@WebListener
public class ConnectionCloser implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent e) {
        ConnectionHolder.close();
    }

    @Override public void requestInitialized(ServletRequestEvent e) {}
}
