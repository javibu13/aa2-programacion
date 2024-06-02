package org.aa2.dao;

import org.aa2.domain.Producto;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ProductoDao {

    @SqlQuery("SELECT * FROM Producto")
    List<Producto> getAllProductos();

    @SqlQuery("SELECT p.*, COUNT(e.Id) as numElementos FROM Producto as p LEFT JOIN Elemento as e ON p.Id = e.ProductoId GROUP BY p.Id ORDER BY p.Id DESC")
    List<Producto> getAllProductosCountElementos();

    @SqlQuery("SELECT * FROM Producto WHERE Id LIKE CONCAT('%', :filtro, '%') OR Nombre LIKE CONCAT('%', :filtro, '%')")
    List<Producto> getProductosByFilter(@Bind("filtro") String filtro);

    @SqlQuery("SELECT * FROM Producto WHERE Id = :id")
    Producto getProductoById(@Bind("id") String id);

    @SqlUpdate("INSERT INTO Producto (Id, Nombre, Descripcion, LimiteUso) VALUES (:id, :nombre, :descripcion, :limiteUso)")
    void addProducto(@Bind("id") String id, @Bind("nombre") String nombre, @Bind("descripcion") String descripcion, @Bind("limiteUso") int limiteUso);

    @SqlUpdate("UPDATE Producto SET Nombre = :nombre, Descripcion = :descripcion, LimiteUso = :limiteUso WHERE Id = :id")
    void updateProducto(@Bind("id") String id, @Bind("nombre") String nombre, @Bind("descripcion") String descripcion, @Bind("limiteUso") int limiteUso);

    @SqlUpdate("DELETE FROM Producto WHERE Id = :id")
    void removeProducto(@Bind("id") String id);

    @SqlQuery("SELECT COUNT(*) FROM Producto WHERE Id = :id")
    boolean idExists(@Bind("id") String id);

    @SqlQuery("SELECT COUNT(*) FROM Producto WHERE Nombre = :nombre")
    boolean nombreExists(@Bind("nombre") String nombre);
}
