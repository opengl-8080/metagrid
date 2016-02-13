package com.github.gl8080.metagrid.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.github.gl8080.metagrid.core.MetaGridException;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.DatabaseType;
import com.github.gl8080.metagrid.core.infrastructure.jdbc.Sql;

public class SqlResolver {

    public Sql resolve(DatabaseType databaseType, Class<?> clazz, String fileName) {
        Objects.requireNonNull(databaseType);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fileName);
        
        String path = "/db/sql/" + databaseType.name().toLowerCase() + "/" + clazz.getSimpleName() + "/" + fileName + ".sql";
        
        try (InputStream in = this.getClass().getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("SQL ファイルが見つかりません : " + path);
            }
            
            String text = IOUtil.toString(in);
            return new Sql(text);
        } catch (IOException e) {
            throw new MetaGridException(e);
        }
    }
    
    
}
