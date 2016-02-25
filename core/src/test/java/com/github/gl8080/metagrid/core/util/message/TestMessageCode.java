package com.github.gl8080.metagrid.core.util.message;

public enum TestMessageCode implements MessageCode {
    TEST_MESSAGE("test.message"),
    BIND_PARAMETER("bind.parameter"),
    ONLY_DEFAULT("test.only.default"),
    ONLY_CUSTOM("test.only.custom"),
    DUPLICATE_DEFINED("test.duplicate.defined")
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
