package com.miempresa.franquicias.service.impl;

import com.miempresa.franquicias.entity.Franquicia;
import com.miempresa.franquicias.entity.Producto;
import com.miempresa.franquicias.entity.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FranquiciaServiceImplTest {

    private FranquiciaRepository franquiciaRepository;
    private FranquiciaServiceImpl franquiciaService;

    @BeforeEach
    void setUp() {
        franquiciaRepository = Mockito.mock(FranquiciaRepository.class);
        franquiciaService = new FranquiciaServiceImpl(franquiciaRepository);
    }

    @Test
    void crearFranquicia_debeGuardarYRetornarFranquicia() {
        Franquicia franquicia = new Franquicia(null, "Café X", null);
        Franquicia guardada = new Franquicia("1", "Café X", null);

        when(franquiciaRepository.save(franquicia)).thenReturn(Mono.just(guardada));

        StepVerifier.create(franquiciaService.crearFranquicia(franquicia))
                .expectNext(guardada)
                .verifyComplete();

        verify(franquiciaRepository).save(franquicia);
    }

    @Test
    void agregarSucursal_debeAgregarSucursalANuevaFranquicia() {
        String franquiciaId = "1";
        String nombreSucursal = "Sucursal Norte";
        Franquicia franquicia = new Franquicia(franquiciaId, "Café X", null);
        Franquicia actualizado = new Franquicia(franquiciaId, "Café X",
                List.of(new Sucursal(nombreSucursal, List.of())));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(actualizado));

        StepVerifier.create(franquiciaService.agregarSucursal(franquiciaId, nombreSucursal))
                .expectNextMatches(f -> f.getSucursales().getFirst().getNombre().equals(nombreSucursal))
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void agregarProducto_debeAgregarProductoASucursalExistente() {
        String franquiciaId = "1";
        String nombreSucursal = "Norte";
        String nombreProducto = "Latte";
        int stock = 10;

        List<Producto> productos = new ArrayList<>();
        Sucursal sucursal = new Sucursal(nombreSucursal, productos);
        Franquicia franquicia = new Franquicia(franquiciaId, "Café X", new ArrayList<>(List.of(sucursal)));

        Producto productoAgregado = new Producto(nombreProducto, stock);
        sucursal.getProductos().add(productoAgregado);

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaService.agregarProducto(franquiciaId, nombreSucursal, nombreProducto, stock))
                .expectNextMatches(f -> f.getSucursales().stream()
                        .anyMatch(s -> s.getNombre().equalsIgnoreCase(nombreSucursal) &&
                                s.getProductos().stream()
                                        .anyMatch(p -> p.getNombre().equals(nombreProducto) && p.getStock() == stock)))
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void eliminarProducto_debeEliminarProductoDeSucursal() {
        String franquiciaId = "1";
        String nombreSucursal = "Sucursal Norte";
        String nombreProducto = "Latte";

        Producto producto = new Producto(nombreProducto, 10);
        Sucursal sucursal = new Sucursal(nombreSucursal, new ArrayList<>(List.of(producto)));
        Franquicia franquicia = new Franquicia(franquiciaId, "Café X", List.of(sucursal));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaService.eliminarProducto(franquiciaId, nombreSucursal, nombreProducto))
                .expectNextMatches(f -> f.getSucursales().getFirst().getProductos().isEmpty())
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void modificarStock_debeActualizarStockDeProducto() {
        String franquiciaId = "1";
        String nombreSucursal = "Sucursal Norte";
        String nombreProducto = "Latte";
        int nuevoStock = 20;

        Producto producto = new Producto(nombreProducto, 10);
        Sucursal sucursal = new Sucursal(nombreSucursal, new ArrayList<>(List.of(producto)));
        Franquicia franquicia = new Franquicia(franquiciaId, "Café X", List.of(sucursal));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaService.modificarStock(franquiciaId, nombreSucursal, nombreProducto, nuevoStock))
                .expectNextMatches(f -> f.getSucursales().getFirst().getProductos().getFirst().getStock() == nuevoStock)
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void obtenerProductoConMasStockPorSucursal_debeRetornarProductosConMasStock() {
        String franquiciaId = "1";

        Producto p1 = new Producto("Latte", 10);
        Producto p2 = new Producto("Capuccino", 15);
        Producto p3 = new Producto("Té", 8);

        Sucursal sucursal1 = new Sucursal("Norte", List.of(p1, p2));
        Sucursal sucursal2 = new Sucursal("Sur", List.of(p3));

        Franquicia franquicia = new Franquicia(franquiciaId, "Café X", List.of(sucursal1, sucursal2));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaService.obtenerProductoConMasStockPorSucursal(franquiciaId))
                .expectNext("Sucursal: Norte -> Producto: Capuccino(Stock: 15)")
                .expectNext("Sucursal: Sur -> Producto: Té(Stock: 8)")
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
    }

    @Test
    void actualizarNombreFranquicia_debeActualizarNombreCorrectamente() {
        String franquiciaId = "1";
        String nuevoNombre = "Café Moderno";
        Franquicia original = new Franquicia(franquiciaId, "Café Antiguo", List.of());

        Franquicia actualizado = new Franquicia(franquiciaId, nuevoNombre, List.of());

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(original));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(actualizado));

        StepVerifier.create(franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre))
                .expectNextMatches(f -> f.getNombre().equals(nuevoNombre))
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void actualizarNombreSucursal_debeActualizarNombreDeSucursalCorrectamente() {
        String franquiciaId = "1";
        String nombreActual = "Norte";
        String nuevoNombre = "Centro";

        Sucursal sucursal = new Sucursal(nombreActual, List.of());
        Franquicia original = new Franquicia(franquiciaId, "Café X", new ArrayList<>(List.of(sucursal)));

        sucursal.setNombre(nuevoNombre);
        Franquicia actualizado = new Franquicia(franquiciaId, "Café X", List.of(sucursal));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(original));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(actualizado));

        StepVerifier.create(franquiciaService.actualizarNombreSucursal(franquiciaId, nombreActual, nuevoNombre))
                .expectNextMatches(f -> f.getSucursales().getFirst().getNombre().equals(nuevoNombre))
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }

    @Test
    void actualizarNombreProducto_debeActualizarNombreDeProductoCorrectamente() {
        String franquiciaId = "1";
        String nombreSucursal = "Norte";
        String nombreProductoActual = "Latte";
        String nuevoNombre = "Latte Especial";

        Producto producto = new Producto(nombreProductoActual, 10);
        Sucursal sucursal = new Sucursal(nombreSucursal, new ArrayList<>(List.of(producto)));
        Franquicia original = new Franquicia(franquiciaId, "Café X", new ArrayList<>(List.of(sucursal)));

        producto.setNombre(nuevoNombre);
        Franquicia actualizado = new Franquicia(franquiciaId, "Café X", List.of(sucursal));

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(original));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(actualizado));

        StepVerifier.create(franquiciaService.actualizarNombreProducto(franquiciaId, nombreSucursal, nombreProductoActual, nuevoNombre))
                .expectNextMatches(f -> f.getSucursales().getFirst().getProductos().getFirst().getNombre().equals(nuevoNombre))
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(franquiciaRepository).save(any(Franquicia.class));
    }
}
