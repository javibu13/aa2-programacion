package org.aa2.dao;

import org.aa2.domain.Elemento;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ElementoMapper implements RowMapper<Elemento> {
    @Override
    public Elemento map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Elemento(
                rs.getInt("Id"),
                rs.getString("ProductoId"),
                rs.getString("NumSerie"),
                rs.getString("Estado")
        );
    }
}
