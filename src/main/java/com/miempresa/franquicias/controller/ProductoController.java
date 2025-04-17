package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.dto.ProductoDto;
import com.miempresa.franquicias.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    public ProductoDto crear(@RequestBody ProductoDto dto) {
        return service.crearProducto(dto);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public List<ProductoDto> listarPorSucursal(@PathVariable Long sucursalId) {
        return service.listarPorSucursal(sucursalId);
    }

    @DeleteMapping("/{productoId}")
    public void eliminar(@PathVariable Long productoId) {
        service.eliminarProducto(productoId);
    }

    @PatchMapping("/{productoId}/stock")
    public ProductoDto actualizarStock(@PathVariable Long productoId, @RequestParam Integer nuevoStock) {
        return service.actualizarStock(productoId, nuevoStock);
    }

    @GetMapping("/top-stock/franquicia/{franquiciaId}")
    public List<ProductoDto> topStockPorFranquicia(@PathVariable Long franquiciaId) {
        return service.obtenerTopStockPorSucursalDeFranquicia(franquiciaId);
    }
}
