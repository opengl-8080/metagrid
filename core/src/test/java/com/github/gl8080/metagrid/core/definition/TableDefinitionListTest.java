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
import com.github.gl8080.metagrid.core.domain.definition.actual.ActualTableDefinition;
import com.github.gl8080.metagrid.core.domain.definition.meta.MetaTableDefinition;

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
        TableDefinition table = MetaTableDefinition.of(ActualTableDefinition.of("hoge").build());
        tableList.add(table);
        
        // exercise
        boolean isEmpty = tableList.isEmpty();
        
        // verify
        assertThat(isEmpty).isFalse();
    }
    
    @Test
    public void 拡張for文で追加したテーブルをイテレートできる() throws Exception {
        // setup
        tableList.add(MetaTableDefinition.of(ActualTableDefinition.of("hoge").build()));
        tableList.add(MetaTableDefinition.of(ActualTableDefinition.of("fuga").build()));
        
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
}
