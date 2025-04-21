package com.miempresa.franquicias.service;

import com.miempresa.franquicias.entity.Franquicia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaService {

    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

    Mono<Franquicia> agregarSucursal(String franquiciaId, String nombreSucursal);

    Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalNombre, String nombreProducto, int stock);

    Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalNombre, String nombreProducto);

    Mono<Franquicia> modificarStock(String franquiciaId, String sucursalNombre, String nombreProducto, int nuevoStock);

    Flux<String> obtenerProductoConMasStockPorSucursal(String franquiciaId);
}
