package com.github.gl8080.metagrid.core.util;

import java.util.Date;

public class Now {
    
    public static long millis() {
        return System.currentTimeMillis();
    }
    
    public static Date datetime() {
        return new Date();
    }
}
