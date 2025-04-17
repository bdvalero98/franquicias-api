package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.dto.ActualizarNombreRequest;
import com.miempresa.franquicias.dto.ProductoDto;
import com.miempresa.franquicias.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ProductoDto crear(@RequestBody ProductoDto dto) {
        return productoService.crearProducto(dto);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public List<ProductoDto> listarPorSucursal(@PathVariable Long sucursalId) {
        return productoService.listarPorSucursal(sucursalId);
    }

    @DeleteMapping("/{productoId}")
    public void eliminar(@PathVariable Long productoId) {
        productoService.eliminarProducto(productoId);
    }

    @PatchMapping("/{productoId}/stock")
    public ProductoDto actualizarStock(@PathVariable Long productoId, @RequestParam Integer nuevoStock) {
        return productoService.actualizarStock(productoId, nuevoStock);
    }

    @GetMapping("/top-stock/franquicia/{franquiciaId}")
    public List<ProductoDto> topStockPorFranquicia(@PathVariable Long franquiciaId) {
        return productoService.obtenerTopStockPorSucursalDeFranquicia(franquiciaId);
    }

    @PatchMapping("/{id}/nombre")
    public ProductoDto actualizarNombreProducto(
            @PathVariable Long id,
            @RequestBody ActualizarNombreRequest request) {
        return productoService.actualizarNombre(id, request.getNuevoNombre());
    }

}
