package com.github.gl8080.metagrid.core.util.message;

import java.text.MessageFormat;
import java.util.Locale;
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
    
    private Locale locale;
    private String defaultBundleBase;
    private String customBundleBase;

    public ResourceBundleHelper(String base) {
        this(Locale.getDefault(), base);
    }
    
    public ResourceBundleHelper(Locale locale, String base) {
        this(locale, base, null);
    }

    public ResourceBundleHelper(String defaultBundle, String customBundle) {
        this(Locale.getDefault(), defaultBundle, customBundle);
    }

    public ResourceBundleHelper(Locale locale, String defaultBundle, String customBundle) {
        this.locale = locale;
        this.defaultBundleBase = defaultBundle;
        this.customBundleBase = customBundle;
    }

    public String getMessage(MessageCode messageCode, Object... arguments) {
        return this.getMessage(this.locale, messageCode, arguments);
    }

    public String getMessage(Locale locale, MessageCode messageCode, Object... arguments) {
        String raw = this.getMessage(locale, messageCode);
        
        if (arguments.length == 0) {
            return raw;
        } else {
            return MessageFormat.format(raw, arguments);
        }
    }
    
    private String getMessage(Locale locale, MessageCode messageCode) {
        if (this.customBundleBase == null) {
            return this.getMessage(this.defaultBundleBase, messageCode, locale);
        }
        
        try {
            return this.getMessage(this.customBundleBase, messageCode, locale);
        } catch (MissingResourceException e) {
            return this.getMessage(this.defaultBundleBase, messageCode, locale);
        }
    }
    
    private String getMessage(String base, MessageCode messageCode, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(base, locale);
        return bundle.getString(messageCode.getKey());
    }
}
