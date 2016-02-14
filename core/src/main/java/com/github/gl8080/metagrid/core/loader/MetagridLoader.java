package com.github.gl8080.metagrid.core.loader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MetagridLoader {
    
    public static void main(String[] args) {
        HttpClient client = new HttpClient("http://localhost:8080/metagrid/api/meta-table-definition");
        Path csv = Paths.get(args[0]);
        client.putHeader("File-Name", csv.toFile().getName());
        client.setCsv(csv);
        Response response = client.put();
        System.out.println(response.getStatus());
        System.out.println(response.getBody());
    }
}
