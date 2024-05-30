package org.aa2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Elemento {
    private int id;
    private String productoId;
    private String numSerie;
    private String estado;
}
