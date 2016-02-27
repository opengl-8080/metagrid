package com.github.gl8080.metagrid.core.util.message;

public enum TestMessageCode implements MessageCode {
    TEST_MESSAGE("test.message"),
    CUSTOM_MESSAGE("test.custom.message"),
    DEFAULT_MESSAGE("test.default.message"),
    CUSTOM_ONLY_MESSAGE("test.custom.only.message"),
    DEFAULT_ONLY_MESSAGE("test.default.only.message"),
    DUPLICATED_MESSAGE("test.duplicated.message"),
    BIND_PARAMETER("bind.parameter")
    ;

    private final String key;
    
    private TestMessageCode(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
