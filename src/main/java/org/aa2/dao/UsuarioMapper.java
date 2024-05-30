package org.aa2.dao;
import org.aa2.domain.Usuario;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioMapper implements RowMapper<Usuario> {
    @Override
    public Usuario map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Usuario(
                rs.getInt("Id"),
                rs.getString("Nombre"),
                rs.getString("Correo"),
                rs.getString("Password"),
                rs.getString("AvatarPath"),
                rs.getString("Rol")
        );
    }
}
