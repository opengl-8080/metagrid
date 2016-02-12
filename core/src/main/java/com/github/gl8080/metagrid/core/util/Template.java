package com.github.gl8080.metagrid.core.util;

import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class Template {
    
    private String template;
    
    public void setTemplate(String template) {
        Objects.requireNonNull(template);
        this.template = template;
    }

    public String render(Map<String, Object> parameter) {
        Objects.requireNonNull(parameter);
        if (this.template == null) {
            throw new IllegalStateException("テンプレートが設定されていません。");
        }
        
        String text = this.template;
        
        for (Entry<String, Object> entry : parameter.entrySet()) {
            String key = "${" + entry.getKey() + "}";
            String value = entry.getValue() == null ? "" : entry.getValue().toString();
            
            text = text.replace(key, value);
        }
        
        return text;
    }
    
}
