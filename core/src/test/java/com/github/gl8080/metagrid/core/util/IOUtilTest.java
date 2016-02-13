package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilTest {

    @Test
    public void test() throws Exception {
        InputStream in = new ByteArrayInputStream("あいうえお\nabcdefg".getBytes("UTF-8"));
        
        String actual = IOUtil.toString(in);
        
        assertThat(actual).isEqualTo("あいうえお\nabcdefg");
    }

}
