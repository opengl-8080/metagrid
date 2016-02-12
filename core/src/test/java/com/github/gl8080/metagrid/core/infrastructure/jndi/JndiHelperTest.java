package com.github.gl8080.metagrid.core.infrastructure.jndi;

import static org.assertj.core.api.Assertions.*;

import javax.naming.InitialContext;

import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class JndiHelperTest {
    
    @Mocked
    private InitialContext context;
    
    @Test
    public void 指定したキーでlookupした値を返す() throws Exception {
        // setup
        JndiHelper helper = new JndiHelper();
        
        new NonStrictExpectations() {{
            context.lookup("xyz"); result = "abc";
        }};
        
        // exercise
        String actual = helper.lookup("xyz");
        
        // verify
        assertThat(actual).isEqualTo("abc");
    }

}
