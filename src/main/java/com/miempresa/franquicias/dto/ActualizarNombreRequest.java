package com.miempresa.franquicias.dto;

import jakarta.validation.constraints.NotBlank;

public class ActualizarNombreRequest {

    @NotBlank(message = "El nuevo nombre no puede estar vacío")
    private String nuevoNombre;

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }
}
