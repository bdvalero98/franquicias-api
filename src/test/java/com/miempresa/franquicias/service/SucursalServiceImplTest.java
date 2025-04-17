package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.SucursalDto;
import com.miempresa.franquicias.exception.NotFoundException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.model.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class SucursalServiceImplTest {

    @Mock
    private FranquiciaRepository franquiciaRepo;

    @Mock
    private SucursalRepository sucursalRepo;

    @InjectMocks
    private SucursalServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearSucursal_CuandoLaFranquiciaExiste_DeberiaCrearSucursal() {
        Long franquiciaId = 1L;
        SucursalDto dto = new SucursalDto(null, "Sucursal Centro", franquiciaId);
        Franquicia franquicia = new Franquicia();
        franquicia.setId(franquiciaId);
        franquicia.setNombre("Franquicia Prueba");

        when(franquiciaRepo.findById(franquiciaId)).thenReturn(Optional.of(franquicia));
        when(sucursalRepo.save(any(Sucursal.class))).thenAnswer(invocation -> {
            Sucursal sucursal = invocation.getArgument(0);
            sucursal.setId(10L);
            return sucursal;
        });

        SucursalDto resultado = service.crearSucursal(dto);

        assertNotNull(resultado.getId());
        assertEquals("Sucursal Centro", resultado.getNombre());
        assertEquals(franquiciaId, resultado.getFranquiciaId());
    }

    @Test
    void crearSucursal_CuandoLaFranquiciaNoExiste_DeberiaLanzarNotFound() {
        Long franquiciaId = 99L;
        SucursalDto dto = new SucursalDto(null, "Sucursal X", franquiciaId);

        when(franquiciaRepo.findById(franquiciaId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.crearSucursal(dto));
    }

    @Test
    void listarPorFranquicia_CuandoExiste_DeberiaRetornarSucursales() {
        Long franquiciaId = 1L;
        Franquicia franquicia = new Franquicia();
        franquicia.setId(franquiciaId);

        Sucursal suc1 = new Sucursal(1L, "Sucursal A", franquicia, new ArrayList<>());
        Sucursal suc2 = new Sucursal(2L, "Sucursal B", franquicia, new ArrayList<>());

        when(franquiciaRepo.existsById(franquiciaId)).thenReturn(true);
        when(sucursalRepo.findByFranquiciaId(franquiciaId)).thenReturn(List.of(suc1, suc2));

        List<SucursalDto> resultado = service.listarSucursalesPorFranquicia(franquiciaId);

        assertEquals(2, resultado.size());
        assertEquals("Sucursal A", resultado.get(0).getNombre());
        assertEquals("Sucursal B", resultado.get(1).getNombre());
    }

    @Test
    void listarPorFranquicia_CuandoNoExiste_DeberiaLanzarNotFound() {
        Long franquiciaId = 999L;
        when(franquiciaRepo.existsById(franquiciaId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.listarSucursalesPorFranquicia(franquiciaId));
    }
}
