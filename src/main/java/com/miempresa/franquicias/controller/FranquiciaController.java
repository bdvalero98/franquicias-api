package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.dto.FranquiciaDto;
import com.miempresa.franquicias.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
@Validated
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    public ResponseEntity<FranquiciaDto> crearFranquicia(@RequestBody FranquiciaDto franquiciaDto) {
        FranquiciaDto nuevaFranquicia = franquiciaService.crearFranquicia(franquiciaDto);
        return ResponseEntity.status(201).body(nuevaFranquicia);
    }

    @GetMapping("/{id}")
    public FranquiciaDto obtenerFranquiciaPorId(@PathVariable Long id) {
        return franquiciaService.obtenerFranquiciaPorId(id);
    }


}
