package org.aa2.domain;

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
}
