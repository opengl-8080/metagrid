package com.github.gl8080.metagrid.core.loader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gl8080.metagrid.core.MetaGridException;

public class HttpClient {
    
    private URL url;
    private Path file;
    private Map<String, String> headers = new HashMap<>();
    
    public HttpClient(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new MetaGridException(e);
        }
    }
    
    public void putHeader(String key, String value) {
        this.headers.put(key, value);
    }
    
    public void setCsv(Path file) {
        this.file = file;
        this.putHeader("Content-Type", "text/csv");
    }
    
    public Response put() {
        try {
            HttpURLConnection con = (HttpURLConnection)this.url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            
            for (Entry<String, String> header : this.headers.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            
            if (this.file != null) {
                try (OutputStream out = con.getOutputStream()) {
                    Files.copy(this.file, out);
                }
            }
            
            int status = con.getResponseCode();
            return new Response(status);
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }
}
