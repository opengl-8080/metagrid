package com.github.gl8080.metagrid.core.domain.definition.meta;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition.Builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class MetaTableDefinitionTest {
    
    public static class Base {
        public Builder actualBuilder;
        
        @Before
        public void setup() {
            actualBuilder = ActualTableDefinition.of("TEST");
        }
    }
    
    public class 物理名 extends Base {
        @Test
        public void 実テーブル定義と同じ値を返す() {
            // setup
            ActualTableDefinition actualDef = actualBuilder.build();
            MetaTableDefinition metaDef = MetaTableDefinition.from(actualDef);
            
            // verify
            assertThat(metaDef.getPhysicalName()).isEqualTo(actualDef.getPhysicalName());
        }
    }
    
    public class 論理名 extends Base {
        
        @Test
        public void 実定義_メタ定義ともに設定されていない場合_null() throws Exception {
            // setup
            MetaTableDefinition metaDef = MetaTableDefinition.from(actualBuilder.build());
            
            // verify
            assertThat(metaDef.getLogicalName()).isNull();
        }
        
        @Test
        public void 実定義が設定されているが_メタ定義が設定されていない場合は_実定義の値() throws Exception {
            // setup
            ActualTableDefinition actualDef = actualBuilder.logicalName("test").build();
            MetaTableDefinition metaDef = MetaTableDefinition.from(actualDef);
            
            // verify
            assertThat(metaDef.getLogicalName()).isEqualTo(actualDef.getLogicalName());
        }
        
        @Test
        public void 実定義_メタ定義両方に設定されている場合は_メタ定義の値() throws Exception {
            // setup
            ActualTableDefinition actualDef = actualBuilder.logicalName("test").build();
            MetaTableDefinition metaDef = MetaTableDefinition.from(actualDef);
            metaDef.setLogicalName("Test");
            
            // verify
            assertThat(metaDef.getLogicalName()).isEqualTo("Test");
        }
    }
    
    
    

}
