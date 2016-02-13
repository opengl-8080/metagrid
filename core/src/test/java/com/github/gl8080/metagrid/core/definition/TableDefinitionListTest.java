package com.github.gl8080.metagrid.core.definition;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.gl8080.metagrid.core.domain.definition.TableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.TableDefinitionList;

public class TableDefinitionListTest {
    
    private TableDefinitionList tableList;
    
    @Before
    public void setup() {
        tableList = new TableDefinitionList();
    }
    
    @Test
    public void インスタンスを作った段階では空() {
        // exercise
        boolean isEmpty = tableList.isEmpty();
        
        // verify
        assertThat(isEmpty).isTrue();
    }
    
    @Test
    public void テーブルを追加したら空ではなくなる() throws Exception {
        // setup
        tableList.add(tableDef("hoge"));
        
        // exercise
        boolean isEmpty = tableList.isEmpty();
        
        // verify
        assertThat(isEmpty).isFalse();
    }
    
    @Test
    public void 拡張for文で追加したテーブルをイテレートできる() throws Exception {
        // setup
        tableList.add(tableDef("hoge"));
        tableList.add(tableDef("fuga"));
        
        // exercise
        List<String> names = new ArrayList<>();
        for (TableDefinition table : tableList) {
            names.add(table.getPhysicalName());
        }
        
        // verify
        assertThat(names).contains("hoge", "fuga");
    }
    
    @Rule
    public ExpectedException ex = ExpectedException.none();
    
    @Test
    public void nullをテーブルとして追加しようとするとエラーになること() throws Exception {
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        tableList.add(null);
    }
    
    public static TableDefinition tableDef(String physicalName) {
        return new DummyTableDefinition(physicalName, null);
    }
    
    public static class DummyTableDefinition implements TableDefinition {
        private String physicalName;
        private String logicalName;
        
        public DummyTableDefinition(String physicalName, String logicalName) {
            this.physicalName = physicalName;
            this.logicalName = logicalName;
        }
        
        @Override
        public String getPhysicalName() {
            return physicalName;
        }
        @Override
        public String getLogicalName() {
            return logicalName;
        }
    }
}
