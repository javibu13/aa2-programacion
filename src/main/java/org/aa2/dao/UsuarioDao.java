package org.aa2.dao;

import org.aa2.domain.Usuario;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UsuarioDao {

    @SqlQuery("SELECT * FROM Usuario")
    List<Usuario> getAllUsuarios();

    @SqlQuery("SELECT COUNT(*) FROM Usuario")
    int getUsuariosCount();

    @SqlQuery("SELECT * FROM Usuario WHERE Id LIKE CONCAT('%', :filtro, '%') OR Nombre LIKE CONCAT('%', :filtro, '%') OR Correo LIKE CONCAT('%', :filtro, '%') OR Rol LIKE :filtro")
    List<Usuario> getUsuariosByFilter(@Bind("filtro") String filtro);

    @SqlQuery("SELECT * FROM Usuario WHERE Id = :id")
    Usuario getUsuarioById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM Usuario WHERE Correo = :correo")
    Usuario getUsuarioByCorreo(@Bind("correo") String correo);

    @SqlUpdate("INSERT INTO Usuario (Nombre, Correo, Password, AvatarPath, Rol) VALUES (:nombre, :correo, :password, :avatarPath, :rol)")
    void addUsuario(@Bind("nombre") String nombre, @Bind("correo") String correo, @Bind("password") String password, @Bind("avatarPath") String avatarPath, @Bind("rol") String rol);

    @SqlUpdate("UPDATE Usuario SET Nombre = :nombre, Correo = :correo, Password = :password, Rol = :rol WHERE Id = :id")
    void updateUsuario(@Bind("id") int id, @Bind("nombre") String nombre, @Bind("correo") String correo, @Bind("password") String password, @Bind("rol") String rol);

    @SqlUpdate("UPDATE Usuario SET Nombre = :nombre, Correo = :correo, Rol = :rol WHERE Id = :id")
    void updateUsuarioNoPass(@Bind("id") int id, @Bind("nombre") String nombre, @Bind("correo") String correo, @Bind("rol") String rol);

    @SqlUpdate("DELETE FROM Usuario WHERE Id = :id")
    void removeUsuario(@Bind("id") int id);

    @SqlQuery("SELECT COUNT(*) FROM Usuario WHERE Correo = :correo")
    boolean correoExists(@Bind("correo") String correo);

    @SqlQuery("SELECT COUNT(*) FROM Usuario WHERE Id = :id")
    boolean idExists(@Bind("id") int id);
}
