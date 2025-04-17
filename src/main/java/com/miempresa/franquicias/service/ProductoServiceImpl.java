package com.miempresa.franquicias.service;

import com.miempresa.franquicias.dto.ProductoDto;
import com.miempresa.franquicias.exception.NotFoundException;
import com.miempresa.franquicias.model.Franquicia;
import com.miempresa.franquicias.model.Producto;
import com.miempresa.franquicias.model.Sucursal;
import com.miempresa.franquicias.repository.FranquiciaRepository;
import com.miempresa.franquicias.repository.ProductoRepository;
import com.miempresa.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    @Override
    public ProductoDto crearProducto(ProductoDto dto) {
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .stock(dto.getStock())
                .sucursal(sucursal)
                .build();

        Producto guardado = productoRepository.save(producto);

        return new ProductoDto(guardado.getId(), guardado.getNombre(), guardado.getStock(), sucursal.getId());
    }

    @Override
    public List<ProductoDto> listarPorSucursal(Long sucursalId) {
        if (!sucursalRepository.existsById(sucursalId)) {
            throw new NotFoundException("Sucursal no existe");
        }

        return productoRepository.findBySucursalId(sucursalId).stream()
                .map(p -> new ProductoDto(p.getId(), p.getNombre(), p.getStock(), p.getSucursal().getId()))
                .toList();
    }

    @Override
    public List<ProductoDto> obtenerTopStockPorSucursalDeFranquicia(Long franquiciaId) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(
                        () -> new NotFoundException("Franquicia no encontrada")
                );

        return franquicia.getSucursales()
                .stream()
                .map(
                        sucursal -> productoRepository.findBySucursalId(sucursal.getId()).stream()
                                .max(Comparator.comparingInt(Producto::getStock))
                                .map(
                                        producto -> new ProductoDto(producto.getId(), producto.getNombre(),
                                                producto.getStock(), sucursal.getId()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public void eliminarProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new NotFoundException("Producto no existe");
        }
        productoRepository.deleteById(productoId);
    }

    @Override
    public ProductoDto actualizarStock(Long productoId, Integer nuevoStock) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        producto.setStock(nuevoStock);
        productoRepository.save(producto);

        return new ProductoDto(producto.getId(), producto.getNombre(), producto.getStock(), producto.getSucursal().getId());
    }

    @Override
    public ProductoDto actualizarNombre(Long productoId, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        producto.setNombre(nuevoNombre);
        Producto actualizado = productoRepository.save(producto);
        return new ProductoDto(actualizado.getId(), actualizado.getNombre(), actualizado.getStock(), actualizado.getSucursal().getId());
    }
}
