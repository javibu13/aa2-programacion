package org.aa2.dao;

import org.aa2.domain.Elemento;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ElementoDao {

    @SqlQuery("SELECT * FROM Elemento")
    List<Elemento> getAllElementos();

    @SqlQuery("SELECT * FROM Elemento WHERE Id = :id")
    Elemento getElementoById(@Bind("id") int id);

    @SqlUpdate("INSERT INTO Elemento (ProductoId, NumSerie, Estado) VALUES (:productoId, :numSerie, :estado)")
    void addElemento(@Bind("productoId") String productoId, @Bind("numSerie") String numSerie, @Bind("estado") String estado);

    @SqlUpdate("UPDATE Elemento SET ProductoId = :productoId, NumSerie = :numSerie, Estado = :estado WHERE Id = :id")
    void updateElemento(@Bind("id") int id, @Bind("productoId") String productoId, @Bind("numSerie") String numSerie, @Bind("estado") String estado);

    @SqlUpdate("DELETE FROM Elemento WHERE Id = :id")
    void removeElemento(@Bind("id") int id);
}
