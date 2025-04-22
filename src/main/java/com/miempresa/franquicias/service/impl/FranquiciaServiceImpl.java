package com.miempresa.franquicias.service.impl;

import com.miempresa.franquicias.entity.Franquicia;
import com.miempresa.franquicias.entity.Producto;
import com.miempresa.franquicias.entity.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import com.miempresa.franquicias.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    @Override
    public Mono<Franquicia> agregarSucursal(String franquiciaId, String nombreSucursal) {
        return franquiciaRepository.findById(franquiciaId)
                .map(
                        franquicia -> {
                            if (franquicia.getSucursales() == null) franquicia.setSucursales(new ArrayList<>());
                            franquicia.getSucursales().add(new Sucursal(nombreSucursal, new ArrayList<>()));
                            return franquicia;
                        }
                )
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalNombre, String nombreProducto, int stock) {
        return franquiciaRepository.findById(franquiciaId)
                .map(
                        franquicia -> {
                            franquicia.getSucursales().forEach(
                                    sucursal -> {
                                        if (sucursal.getNombre().equalsIgnoreCase(sucursalNombre)) {
                                            if (sucursal.getProductos() == null)
                                                sucursal.setProductos(new ArrayList<>());
                                            sucursal.getProductos().add(new Producto(nombreProducto, stock));
                                        }
                                    });
                            return franquicia;
                        })
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalNombre, String nombreProducto) {
        return franquiciaRepository.findById(franquiciaId)
                .map(
                        franquicia -> {
                            franquicia.getSucursales().forEach(
                                    sucursal -> {
                                        if (sucursal.getNombre().equalsIgnoreCase(sucursalNombre)) {
                                            if (sucursal.getProductos() != null) {
                                                sucursal.getProductos().removeIf(
                                                        p -> p.getNombre().equalsIgnoreCase(nombreProducto)
                                                );
                                            }
                                        }
                                    }
                            );
                            return franquicia;
                        }
                )
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> modificarStock(String franquiciaId, String sucursalNombre, String nombreProducto, int nuevoStock) {
        return franquiciaRepository.findById(franquiciaId)
                .map(
                        franquicia -> {
                            franquicia.getSucursales().forEach(
                                    sucursal -> {
                                        if (sucursal.getNombre().equalsIgnoreCase(sucursalNombre)) {
                                            if (sucursal.getProductos() != null) {
                                                sucursal.getProductos().forEach(
                                                        p -> {
                                                            if (p.getNombre().equalsIgnoreCase(nombreProducto)) {
                                                                p.setStock(nuevoStock);
                                                            }
                                                        }
                                                );
                                            }
                                        }
                                    }
                            );
                            return franquicia;
                        }
                )
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Flux<String> obtenerProductoConMasStockPorSucursal(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMapMany(
                        franquicia -> {
                            List<String> result = new ArrayList<>();
                            for (Sucursal sucursal : franquicia.getSucursales()) {
                                if (sucursal.getProductos() != null && !sucursal.getProductos().isEmpty()) {
                                    Producto maxStock = Collections.max(sucursal.getProductos(),
                                            Comparator.comparingInt(Producto::getStock));
                                    result.add("Sucursal: " + sucursal.getNombre() + " -> Producto: " + maxStock.getNombre() + "(Stock: " + maxStock.getStock() + ")");
                                }
                            }
                            return Flux.fromIterable(result);
                        }
                );
    }

    @Override
    public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    franquicia.setNombre(nuevoNombre);
                    return franquicia;
                })
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String nombreSucursalActual,
                                                     String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursalActual))
                            .findFirst()
                            .ifPresent(s -> s.setNombre(nuevoNombre));
                    return franquicia;
                })
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String nombreSucursal,
                                                     String nombreProductoActual, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .map(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursal))
                            .findFirst().flatMap(sucursal -> sucursal.getProductos().stream()
                                    .filter(p -> p.getNombre().equalsIgnoreCase(nombreProductoActual))
                                    .findFirst()).ifPresent(p -> p.setNombre(nuevoNombre));
                    return franquicia;
                })
                .flatMap(franquiciaRepository::save);
    }
}
