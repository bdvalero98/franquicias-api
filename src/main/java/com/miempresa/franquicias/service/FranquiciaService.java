package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.FranquiciaDto;

public interface FranquiciaService {

    FranquiciaDto crearFranquicia(FranquiciaDto franquiciaDto);
    FranquiciaDto obtenerFranquiciaPorId(Long id);
    FranquiciaDto actualizarNombreFranquicia(Long id, String nuevoNombre);
}
