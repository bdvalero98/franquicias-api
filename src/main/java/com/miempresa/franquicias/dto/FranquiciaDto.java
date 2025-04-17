package com.miempresa.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaDto {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
}
