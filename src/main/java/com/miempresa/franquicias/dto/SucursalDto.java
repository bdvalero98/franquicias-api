package com.miempresa.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDto {

    private Long id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    @NotNull(message = "El ID de la franquicia es obligatorio")
    private Long franquiciaId;
}
