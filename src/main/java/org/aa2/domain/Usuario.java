package org.aa2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private String avatarPath;
    private String rol;
}
