package com.github.gl8080.metagrid.core.domain.upload.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.gl8080.metagrid.core.domain.upload.FileLineProcessor;

public class CsvRecordParser implements FileLineProcessor {
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(",(?=(([^\"]*\"){2})*[^\"]*$)");
    private static final Pattern WRAP_PATTERN = Pattern.compile("^\"|\"$");
    private static final Pattern ESCAPED_WRAP_PATTERN = Pattern.compile("\"\"");
    
    private CsvRecordProcessor<List<String>> processor;
    
    public CsvRecordParser(CsvRecordProcessor<List<String>> processor) {
        Objects.requireNonNull(processor);
        this.processor = processor;
    }

    @Override
    public void process(String line) {
        String[] elements = SEPARATOR_PATTERN.split(line, -1);
        List<String> list = new ArrayList<>();
        
        for (String element : elements) {
            Matcher matcher = WRAP_PATTERN.matcher(element);
            element = matcher.replaceAll("");
            
            matcher = ESCAPED_WRAP_PATTERN.matcher(element);
            element = matcher.replaceAll("\"");
            
            list.add(element);
        }
        
        this.processor.process(list);
    }
}
