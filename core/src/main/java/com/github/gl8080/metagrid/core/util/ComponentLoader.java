package com.github.gl8080.metagrid.core.util;

import java.util.ServiceLoader;

public class ComponentLoader {

    public static <T> T getComponent(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        return loader.iterator().next();
    }
}
