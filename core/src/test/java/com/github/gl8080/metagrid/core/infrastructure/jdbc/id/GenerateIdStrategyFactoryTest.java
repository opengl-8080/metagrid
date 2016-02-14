package com.github.gl8080.metagrid.core.infrastructure.jdbc.id;

import static org.assertj.core.api.Assertions.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.JdbcHelper;

import mockit.Mocked;
import mockit.NonStrictExpectations;

public class GenerateIdStrategyFactoryTest {

    @Test
    public void 自動生成() {
        // exercise
        GenerateIdStrategy strategy = GenerateIdStrategyFactory.auto();
        
        // verify
        assertThat(strategy).isInstanceOf(AutoGenerateIdStrategy.class);
    }
    
    @Mocked
    private JdbcHelper jdbc;

    @Test
    public void シーケンス_oracle() {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = DatabaseType.ORACLE;
        }};
        
        // exercise
        GenerateIdStrategy strategy = GenerateIdStrategyFactory.sequence("SEQ_NAME", jdbc);
        
        // verify
        assertThat(strategy).isInstanceOf(OracleSequenceStrategy.class);
    }

    @Test
    public void シーケンス_postgresql() {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = DatabaseType.POSTGRESQL;
        }};
        
        // exercise
        GenerateIdStrategy strategy = GenerateIdStrategyFactory.sequence("SEQ_NAME", jdbc);
        
        // verify
        assertThat(strategy).isInstanceOf(PostgreSQLSequenceStrategy.class);
    }
    
    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void シーケンス_mysql() {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = DatabaseType.MYSQL;
        }};
        
        // verify
        ex.expect(UnsupportedOperationException.class);
        
        // exercise
        GenerateIdStrategyFactory.sequence("SEQ_NAME", jdbc);
    }
    
    @Test
    public void シーケンス_null() {
        // setup
        new NonStrictExpectations() {{
            jdbc.getDatabaseType(); result = null;
        }};
        
        // verify
        ex.expect(NullPointerException.class);
        
        // exercise
        GenerateIdStrategyFactory.sequence("SEQ_NAME", jdbc);
    }
}
