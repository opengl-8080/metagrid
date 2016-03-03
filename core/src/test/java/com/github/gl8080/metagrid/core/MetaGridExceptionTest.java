package com.github.gl8080.metagrid.core;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class MetaGridExceptionTest {

    @Test
    public void 根本原因となった例外を取得できる() {
        // setup
        Exception root = new Exception();
        RuntimeException wrap = new RuntimeException(root);
        MetaGridException ex = new MetaGridException(wrap);
        
        // exercise
        Throwable actual = ex.getRootCause();
        
        // verify
        assertThat(actual).isSameAs(root);
    }
    
    @Test
    public void 原因が無い場合は自分自身を返す() {
        // setup
        MetaGridException ex = new MetaGridException(null);
        
        // exercise
        Throwable actual = ex.getRootCause();
        
        // verify
        assertThat(actual).isSameAs(ex);
    }

}
