package org.aa2.dao;

import org.aa2.domain.Asignacion;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsignacionMapper implements RowMapper<Asignacion> {
    @Override
    public Asignacion map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Asignacion(
                rs.getInt("Id"),
                rs.getInt("ElementoId"),
                rs.getInt("UsuarioId"),
                rs.getDate("FechaAsignacion"),
                rs.getDate("FechaDevolucion")
        );
    }
}
