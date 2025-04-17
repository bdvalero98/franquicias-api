package com.miempresa.franquicias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private Long id;

    private String nombre;

    private Integer stock;

    private Long sucursalId;
}
