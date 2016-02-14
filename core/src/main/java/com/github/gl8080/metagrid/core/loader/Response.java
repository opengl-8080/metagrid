package com.github.gl8080.metagrid.core.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.util.IOUtil;

public class Response {
    
    private HttpURLConnection con;
    
    public Response(HttpURLConnection con) {
        this.con = con;
    }
    
    public int getStatus() {
        try {
            return this.con.getResponseCode();
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }
    
    public String getBody() {
        try (InputStream in = this.getResponseStream()) {
            return IOUtil.toString(in);
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }
    
    private InputStream getResponseStream() throws IOException {
        return (400 <= this.getStatus()) ? this.con.getErrorStream() : this.con.getInputStream();
    }
}
