package com.github.gl8080.metagrid.core.loader;

import java.nio.file.Paths;

public class MetagridLoader {
    
    public static void main(String[] args) {
        HttpClient client = new HttpClient("http://localhost:8080/metagrid/api/meta-table-definition");
        client.setCsv(Paths.get(args[0]));
        client.put();
    }
}
