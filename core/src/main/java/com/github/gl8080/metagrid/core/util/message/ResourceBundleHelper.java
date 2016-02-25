package com.github.gl8080.metagrid.core.util.message;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceBundleHelper {
    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleHelper.class);

    private static final String CUSTOM_BUNDLE_BASE = "metagrid_messages";
    private static final String DEFAULT_BUNDLE_BASE = "metagrid_default_messages";
    private static ResourceBundleHelper instance;
    
    public static void initialize() {
        instance = new ResourceBundleHelper(DEFAULT_BUNDLE_BASE, CUSTOM_BUNDLE_BASE);
        logger.info("リソースバンドルを読み込みました。");
    }
    
    public static ResourceBundleHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("初期化されていません。");
        }
        
        return instance;
    }
    
    private ResourceBundle defaultBundle;
    private ResourceBundle customBundle;
    
    public ResourceBundleHelper(String base) {
        this(base, null);
    }

    public ResourceBundleHelper(String defaultBase, String customBase) {
        this.defaultBundle = ResourceBundle.getBundle(defaultBase);
        
        if (customBase != null) {
            this.customBundle = ResourceBundle.getBundle(customBase);
        }
    }

    public String getMessage(MessageCode messageCode, Object... parameters) {
        String raw;
        
        if (this.customBundle == null) {
            raw = this.defaultBundle.getString(messageCode.getKey());
        } else {
            try {
                raw = this.customBundle.getString(messageCode.getKey());
            } catch (MissingResourceException e) {
                raw = this.defaultBundle.getString(messageCode.getKey());
            }
        }
        
        if (parameters == null || parameters.length == 0) {
            return raw;
        } else {
            return MessageFormat.format(raw, parameters);
        }
    }
}
