package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.FranquiciaDto;
import com.miempresa.franquicias.exception.BadRequestException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranquiciaServiceImplTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaServiceImpl franquiciaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearFranquicia() {
        when(franquiciaRepository.existsByNombre("X"))
                .thenReturn(false);

        Franquicia franquicia = new Franquicia(1L, "X", null);

        when(franquiciaRepository.save(any()))
                .thenReturn(franquicia);

        FranquiciaDto franquiciaDto = franquiciaService.crearFranquicia(new FranquiciaDto(null,"X"));
        assertEquals(1L, franquiciaDto.getId());
        assertEquals("X", franquiciaDto.getNombre());
    }

    @Test
    void crear_duplicada() {
        when(franquiciaRepository.existsByNombre("X"))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> franquiciaService.crearFranquicia(new FranquiciaDto(null,"X"))
        );
    }
}