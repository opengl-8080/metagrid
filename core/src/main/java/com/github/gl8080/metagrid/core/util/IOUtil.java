package com.github.gl8080.metagrid.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.github.gl8080.metagrid.core.MetaGridException;

public class IOUtil {

    public static String toString(InputStream in) {
        return toString(in, StandardCharsets.UTF_8);
    }

    public static String toString(InputStream in, Charset charset) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int tmp;
            
            while ((tmp = in.read()) != -1) {
                out.write(tmp);
            }
            
            return new String(out.toByteArray(), charset);
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }

}
