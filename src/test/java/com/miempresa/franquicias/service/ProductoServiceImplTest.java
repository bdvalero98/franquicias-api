package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.ProductoDto;
import com.miempresa.franquicias.exception.NotFoundException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.model.Producto;
import com.miempresa.franquicias.model.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import com.miempresa.franquicias.repository.ProductoRepository;
import com.miempresa.franquicias.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private ProductoServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearProducto_DeberiaCrearCorrectamente() {
        Sucursal sucursal = new Sucursal(1L, "Sucursal A", null, new ArrayList<>());
        ProductoDto productoDto = new ProductoDto(null, "Café", 50, sucursal.getId());

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.save(any())).thenAnswer(i -> {
            Producto producto = i.getArgument(0);
            producto.setId(100L);
            return producto;
        });

        ProductoDto res = service.crearProducto(productoDto);

        assertNotNull(res.getId());
        assertEquals("Café", res.getNombre());
        assertEquals(50, res.getStock());
    }

    @Test
    void listarPorSucursal_DeberiaRetornarProductos() {
        Long sucursalId = 1L;
        when(sucursalRepository.existsById(sucursalId)).thenReturn(true);

        Producto p1 = new Producto(1L, "Café", 50, new Sucursal(sucursalId, "", null, null));
        Producto p2 = new Producto(2L, "Té", 20, new Sucursal(sucursalId, "", null, null));

        when(productoRepository.findBySucursalId(sucursalId)).thenReturn(List.of(p1, p2));

        List<ProductoDto> res = service.listarPorSucursal(sucursalId);

        assertEquals(2, res.size());
    }

    @Test
    void eliminarProducto_ProductoNoExiste_DeberiaLanzarExcepcion() {
        Long id = 5L;
        when(productoRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.eliminarProducto(id));
    }

    @Test
    void actualizarStock_DeberiaActualizarCorrectamente() {
        Long id = 5L;
        Producto p = new Producto(id, "Chocolate", 10, new Sucursal());

        when(productoRepository.findById(id)).thenReturn(Optional.of(p));

        ProductoDto result = service.actualizarStock(id, 99);

        assertEquals(99, result.getStock());
    }

    @Test
    void obtenerTopStockPorSucursalDeFranquicia_DeberiaRetornarCorrectamente() {
        Franquicia franquicia = new Franquicia(1L, "Franquicia 1", new ArrayList<>());

        Sucursal s1 = new Sucursal(1L, "Sucursal 1", franquicia, new ArrayList<>());
        Sucursal s2 = new Sucursal(2L, "Sucursal 2", franquicia, new ArrayList<>());
        franquicia.setSucursales(List.of(s1, s2));

        Producto p1 = new Producto(1L, "Café", 100, s1);
        Producto p2 = new Producto(2L, "Té", 150, s1);
        Producto p3 = new Producto(3L, "Chocolate", 80, s2);

        when(franquiciaRepository.findById(1L)).thenReturn(Optional.of(franquicia));
        when(productoRepository.findBySucursalId(1L)).thenReturn(List.of(p1, p2));
        when(productoRepository.findBySucursalId(2L)).thenReturn(List.of(p3));

        List<ProductoDto> result = service.obtenerTopStockPorSucursalDeFranquicia(1L);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getNombre().equals("Té")));
        assertTrue(result.stream().anyMatch(p -> p.getNombre().equals("Chocolate")));
    }

}