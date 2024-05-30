package org.aa2.dao;

import org.aa2.domain.Asignacion;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface AsignacionDao {

    @SqlQuery("SELECT * FROM Asignacion")
    List<Asignacion> getAllAsignaciones();

    @SqlQuery("SELECT * FROM Asignacion WHERE Id = :id")
    Asignacion getAsignacionById(@Bind("id") int id);

    @SqlUpdate("INSERT INTO Asignacion (ElementoId, UsuarioId, FechaAsignacion, FechaDevolucion) VALUES (:elementoId, :usuarioId, :fechaAsignacion, :fechaDevolucion)")
    void addAsignacion(@Bind("elementoId") int elementoId, @Bind("usuarioId") int usuarioId, @Bind("fechaAsignacion") java.sql.Date fechaAsignacion, @Bind("fechaDevolucion") java.sql.Date fechaDevolucion);

    @SqlUpdate("UPDATE Asignacion SET ElementoId = :elementoId, UsuarioId = :usuarioId, FechaAsignacion = :fechaAsignacion, FechaDevolucion = :fechaDevolucion WHERE Id = :id")
    void updateAsignacion(@Bind("id") int id, @Bind("elementoId") int elementoId, @Bind("usuarioId") int usuarioId, @Bind("fechaAsignacion") java.sql.Date fechaAsignacion, @Bind("fechaDevolucion") java.sql.Date fechaDevolucion);

    @SqlUpdate("DELETE FROM Asignacion WHERE Id = :id")
    void removeAsignacion(@Bind("id") int id);
}
