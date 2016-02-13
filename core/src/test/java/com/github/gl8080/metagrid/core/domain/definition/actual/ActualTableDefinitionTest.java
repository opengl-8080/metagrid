package com.github.gl8080.metagrid.core.domain.definition.actual;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition.Builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class ActualTableDefinitionTest {
    
    @Rule
    public ExpectedException ex = ExpectedException.none();
    
    public class Builderのテスト {
        
        public static final String PHYSICAL_NAME = "hoge";
        public static final String LOGICAL_NAME = "HOGE";
        
        public Builder builder;
        
        @Before
        public void setup() {
            builder = ActualTableDefinition.of(PHYSICAL_NAME);
        }
        
        @Test
        public void ファクトリメソッドで物理名を指定して_ビルダー経由でインスタンスを作れる() {
            // exercise
            ActualTableDefinition table = builder.build();
            
            // verify
            assertThat(table.getPhysicalName()).isEqualTo(PHYSICAL_NAME);
        }
        
        @Test
        public void 論理名を指定できる() throws Exception {
            // exercise
            ActualTableDefinition table = builder.logicalName(LOGICAL_NAME).build();
            
            // verify
            assertThat(table.getLogicalName()).isEqualTo(LOGICAL_NAME);
        }
        
        @Test
        public void 論理名にnullを指定することは可能() throws Exception {
            // exercise
            ActualTableDefinition table = builder.logicalName(null).build();
            
            // verify
            assertThat(table.getLogicalName()).isNull();
        }
        
        @Test
        public void 物理名にnullを渡すと例外がスローされること() throws Exception {
            // verify
            ex.expect(NullPointerException.class);
            
            // exercise
            ActualTableDefinition.of(null);
        }
        
        @Test
        public void 物理名に空文字を渡すと例外がスローされること() throws Exception {
            // verify
            ex.expect(IllegalArgumentException.class);
            
            // exercise
            ActualTableDefinition.of("");
        }
    }

}
