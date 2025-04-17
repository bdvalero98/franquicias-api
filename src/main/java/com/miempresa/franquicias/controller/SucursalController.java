package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.dto.SucursalDto;
import com.miempresa.franquicias.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<SucursalDto> crearSucursal(@RequestBody SucursalDto sucursalDto) {
        SucursalDto nuevaSucursal = sucursalService.crearSucursal(sucursalDto);
        return ResponseEntity.status(201).body(nuevaSucursal);
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public List<SucursalDto> listarSucursalesPorFranquicia(@PathVariable Long franquiciaId) {
        return sucursalService.listarSucursalesPorFranquicia(franquiciaId);
    }
}
