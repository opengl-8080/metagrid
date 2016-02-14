package com.github.gl8080.metagrid.core.infrastructure.jdbc.id;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

public class OracleSequenceStrategyTest {

    @Mocked
    private JdbcHelper jdbc;
    
    private OracleSequenceStrategy strategy;
    
    @Before
    public void setup() {
        new NonStrictExpectations() {{
            jdbc.querySingle((Sql)any); result = new BigDecimal("10");
        }};
        
        strategy = new OracleSequenceStrategy("SEQ_NAME", jdbc);
    }
    
    @Test
    public void 検索結果がそのまま返される() {
        // exercise
        long id = strategy.generate();
        
        // verify
        assertThat(id).isEqualTo(10);
    }
    
    @Test
    public void シーケンスの次の値を習得するSQLが実行される() {
        // exercise
        strategy.generate();
        
        // verify
        new Verifications() {{
            Sql sql;
            jdbc.querySingle(sql = withCapture());
            
            assertThat(sql.getText()).isEqualTo("SELECT SEQ_NAME.NEXTVAL FROM DUAL");
        }};
    }

}
