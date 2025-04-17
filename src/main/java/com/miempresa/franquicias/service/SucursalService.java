package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.SucursalDto;

import java.util.List;

public interface SucursalService {

    SucursalDto crearSucursal(SucursalDto sucursalDto);

    List<SucursalDto> listarSucursalesPorFranquicia(Long franquiciaId);

    SucursalDto actualizarNombreSucursal(Long sucursalId, String nuevoNombre);
}
