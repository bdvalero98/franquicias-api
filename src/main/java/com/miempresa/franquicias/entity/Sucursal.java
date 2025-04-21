package com.miempresa.franquicias.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private String nombre;

    private List<Producto> productos;
}
