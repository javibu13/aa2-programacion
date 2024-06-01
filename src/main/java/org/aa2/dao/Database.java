package org.aa2.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import static org.aa2.util.Constants.*;

public class Database {
    public static Jdbi jdbi = null;

    public static Jdbi getInstance() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        if (jdbi == null) {
            jdbi = Jdbi.create(CONNECTION_STRING, USERNAME, PASSWORD);
            jdbi.installPlugin(new SqlObjectPlugin());
            jdbi.registerRowMapper(new UsuarioMapper());
            jdbi.registerRowMapper(new AsignacionMapper());
            jdbi.registerRowMapper(new ElementoMapper());
            jdbi.registerRowMapper(new ProductoMapper());
        }
        return jdbi;
    }
}