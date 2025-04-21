package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.entity.Franquicia;
import com.miempresa.franquicias.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaService.crearFranquicia(franquicia);
    }

    @PostMapping("/{franquiciaId}/sucursales")
    public Mono<Franquicia> agregarSucursal(
            @PathVariable String franquiciaId,
            @RequestParam String nombreSucursal) {
        return franquiciaService.agregarSucursal(franquiciaId, nombreSucursal);
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos")
    public Mono<Franquicia> agregarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @RequestParam String nombreProducto,
            @RequestParam int stock) {
        return franquiciaService.agregarProducto(franquiciaId, sucursalNombre, nombreProducto, stock);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos")
    public Mono<Franquicia> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @RequestParam String nombreProducto) {
        return franquiciaService.eliminarProducto(franquiciaId, sucursalNombre, nombreProducto);
    }

    @PatchMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/stock")
    public Mono<Franquicia> modificarStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @RequestParam String nombreProducto,
            @RequestParam int nuevoStock) {
        return franquiciaService.modificarStock(franquiciaId, sucursalNombre, nombreProducto, nuevoStock);
    }

    @GetMapping("/{franquiciaId}/productos-max-stock")
    public Flux<String> obtenerProductoConMasStockPorSucursal(@PathVariable String franquiciaId) {
        return franquiciaService.obtenerProductoConMasStockPorSucursal(franquiciaId);
    }
}
