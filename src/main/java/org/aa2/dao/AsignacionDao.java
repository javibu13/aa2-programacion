package org.aa2.dao;

import org.aa2.domain.Asignacion;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface AsignacionDao {

    @SqlQuery("SELECT * FROM Asignacion")
    List<Asignacion> getAllAsignaciones();

    @SqlQuery("SELECT * FROM Asignacion a JOIN Usuario u ON a.UsuarioId = u.Id JOIN Elemento e ON a.ElementoId = e.Id WHERE a.Id LIKE CONCAT('%', :filter, '%') OR a.ElementoId LIKE CONCAT('%', :filter, '%') OR e.ProductoId LIKE CONCAT('%', :filter, '%') OR u.Nombre LIKE CONCAT('%', :filter, '%')")
    List<Asignacion> getAsignacionesByFilter(@Bind("filter") String filter);

    @SqlQuery("SELECT * FROM Asignacion WHERE Id = :id")
    Asignacion getAsignacionById(@Bind("id") int id);

    @SqlUpdate("INSERT INTO Asignacion (ElementoId, UsuarioId, FechaAsignacion) VALUES (:elementoId, :usuarioId, :fechaAsignacion)")
    void addAsignacion(@Bind("elementoId") int elementoId, @Bind("usuarioId") int usuarioId, @Bind("fechaAsignacion") java.sql.Date fechaAsignacion);

    @SqlUpdate("UPDATE Asignacion SET ElementoId = :elementoId, UsuarioId = :usuarioId, FechaAsignacion = :fechaAsignacion, FechaDevolucion = :fechaDevolucion WHERE Id = :id")
    void updateAsignacion(@Bind("id") int id, @Bind("elementoId") int elementoId, @Bind("usuarioId") int usuarioId, @Bind("fechaAsignacion") java.sql.Date fechaAsignacion, @Bind("fechaDevolucion") java.sql.Date fechaDevolucion);

    @SqlUpdate("UPDATE Asignacion SET FechaAsignacion = :fechaAsignacion, FechaDevolucion = :fechaDevolucion WHERE Id = :id")
    void updateAsignacionFechas(@Bind("id") int id, @Bind("fechaAsignacion") java.sql.Date fechaAsignacion, @Bind("fechaDevolucion") java.sql.Date fechaDevolucion);

    @SqlUpdate("DELETE FROM Asignacion WHERE Id = :id")
    void removeAsignacion(@Bind("id") int id);

    @SqlQuery("SELECT COUNT(*) FROM Asignacion WHERE ElementoId = :elementoId AND FechaDevolucion IS NULL")
    boolean asignacionElementoActivaExists(@Bind("elementoId") int elementoId);

    @SqlQuery("SELECT COUNT(*) FROM Asignacion WHERE Id = :id")
    boolean idExists(@Bind("id") int id);
}
