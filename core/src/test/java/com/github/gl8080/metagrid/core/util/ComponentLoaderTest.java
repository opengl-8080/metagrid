package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.gl8080.metagrid.core.util.test.TestComponent;
import com.github.gl8080.metagrid.core.util.test.TestComponentImpl;

public class ComponentLoaderTest {

    @Test
    public void サービスローダーによって定義されたコンポーネントが取得できる() {
        // exercise
        TestComponent component = ComponentLoader.getComponent(TestComponent.class);
        
        // verify
        assertThat(component).isInstanceOf(TestComponentImpl.class);
    }
}
