package com.github.gl8080.metagrid.core.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetConverter<T> {

    T convert(ResultSet rs) throws SQLException;
}
