package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.SucursalDto;
import com.miempresa.franquicias.exception.NotFoundException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.model.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import com.miempresa.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;


    @Override
    public SucursalDto crearSucursal(SucursalDto sucursalDto) {
        Franquicia franquicia = franquiciaRepository.findById(sucursalDto.getFranquiciaId())
                .orElseThrow(
                        () -> new NotFoundException("Franquicia no encontrada: " + sucursalDto.getFranquiciaId())
                );

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(sucursalDto.getNombre());
        sucursal.setFranquicia(franquicia);

        sucursal = sucursalRepository.save(sucursal);

        return new SucursalDto(sucursal.getId(), sucursal.getNombre(), franquicia.getId());
    }

    @Override
    public List<SucursalDto> listarSucursalesPorFranquicia(Long franquiciaId) {

        if (!franquiciaRepository.existsById(franquiciaId)) {
            throw new NotFoundException("Franquicia no encontrada: " + franquiciaId);
        }

        return sucursalRepository.findByFranquiciaId(franquiciaId)
                .stream()
                .map(
                        s -> new SucursalDto(s.getId(), s.getNombre(), s.getFranquicia().getId())
                )
                .collect(Collectors.toList());
    }

    @Override
    public SucursalDto actualizarNombreSucursal(Long sucursalId, String nuevoNombre) {

        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede ser nulo o vacío");
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(
                        () -> new NotFoundException("Sucursal no encontrada: " + sucursalId)
                );

        sucursal.setNombre(nuevoNombre);
        Sucursal sucursalActualizada = sucursalRepository.save(sucursal);
        return new SucursalDto(sucursalActualizada.getId(), sucursalActualizada.getNombre(), sucursalActualizada.getFranquicia().getId());
    }
}
