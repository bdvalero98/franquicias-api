package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.ProductoDto;

import java.util.List;

public interface ProductoService {
    ProductoDto crearProducto(ProductoDto dto);

    List<ProductoDto> listarPorSucursal(Long sucursalId);

    List<ProductoDto> obtenerTopStockPorSucursalDeFranquicia(Long franquiciaId);

    void eliminarProducto(Long productoId);

    ProductoDto actualizarStock(Long productoId, Integer nuevoStock);

    ProductoDto actualizarNombre(Long productoId, String nuevoNombre);
}
