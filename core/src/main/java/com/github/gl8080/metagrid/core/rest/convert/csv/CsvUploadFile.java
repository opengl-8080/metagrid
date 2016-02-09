package com.github.gl8080.metagrid.core.rest.convert.csv;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.util.ThrowableConsumer;

public class CsvUploadFile {
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(",(?=(([^\"]*\"){2})*[^\"]*$)");
    private static final Pattern WRAP_PATTERN = Pattern.compile("^\"|\"$");
    private static final Pattern ESCAPED_WRAP_PATTERN = Pattern.compile("\"\"");
    
    private InputStream in;

    public CsvUploadFile(InputStream in) {
        Objects.requireNonNull(in);
        this.in = in;
    }

    public void each(ThrowableConsumer<List<String>> processor) {
        this.each(StandardCharsets.UTF_8, processor);
    }

    public void each(Charset charset, ThrowableConsumer<List<String>> processor) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.in, charset))) {
            String line = null;
            
            while ((line = br.readLine()) != null) {
                String[] elements = SEPARATOR_PATTERN.split(line, -1);
                List<String> list = new ArrayList<>();
                
                for (String element : elements) {
                    Matcher matcher = WRAP_PATTERN.matcher(element);
                    element = matcher.replaceAll("");
                    
                    matcher = ESCAPED_WRAP_PATTERN.matcher(element);
                    element = matcher.replaceAll("\"");
                    
                    list.add(element);
                }
                
                processor.consume(list);
            }
        } catch (Exception e) {
            throw new MetaGridException(e);
        }
    }
}
