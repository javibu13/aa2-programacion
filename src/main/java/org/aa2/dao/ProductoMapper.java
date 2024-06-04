package org.aa2.dao;

import org.aa2.domain.Producto;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoMapper implements RowMapper<Producto> {
    @Override
    public Producto map(ResultSet rs, StatementContext ctx) throws SQLException {
        int numElementos;
        try {
            numElementos = rs.getInt("numElementos");
        } catch (SQLException e) {
            numElementos = 0;
        }
        return new Producto(
                rs.getString("Id"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                rs.getInt("LimiteUso"),
                numElementos
        );
    }
}
