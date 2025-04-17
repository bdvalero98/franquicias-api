package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.FranquiciaDto;
import com.miempresa.franquicias.exception.BadRequestException;
import com.miempresa.franquicias.exception.NotFoundException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    public FranquiciaDto crearFranquicia(FranquiciaDto franquiciaDto) {

        if (franquiciaRepository.existsByNombre(franquiciaDto.getNombre())) {
            throw new BadRequestException("Ya existe la franquicia " + franquiciaDto.getNombre());
        }

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(franquiciaDto.getNombre());
        franquicia = franquiciaRepository.save(franquicia);
        return new FranquiciaDto(franquicia.getId(), franquiciaDto.getNombre());
    }

    @Override
    public FranquiciaDto obtenerFranquiciaPorId(Long id) {

        Franquicia franquicia = franquiciaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Franquicia no encontrada: " + id));
        return new FranquiciaDto(franquicia.getId(), franquicia.getNombre());

    }

    @Override
    public FranquiciaDto actualizarNombreFranquicia(Long id, String nuevoNombre) {

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede ser nulo o vacío");
        }

        Franquicia franquicia = franquiciaRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Franquicia no encontrada: " + id)
                );

        franquicia.setNombre(nuevoNombre);
        Franquicia franquiciaActualizada = franquiciaRepository.save(franquicia);

        return new FranquiciaDto(franquiciaActualizada.getId(), franquiciaActualizada.getNombre());
    }
}
