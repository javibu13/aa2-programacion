package org.aa2.domain;

import org.aa2.dao.Database;
import org.aa2.dao.ElementoDao;
import org.aa2.dao.UsuarioDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {
    private int id;
    private int elementoId;
    private int usuarioId;
    private Date fechaAsignacion;
    private Date fechaDevolucion;
    private Elemento elemento;
    private Usuario usuario;

    public void linkRelatedData() throws ClassNotFoundException {
        this.elemento = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
            return elementoDao.getElementoById(this.elementoId);
        });
        this.usuario = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
            return usuarioDao.getUsuarioById(this.usuarioId);
        });
    }
}
