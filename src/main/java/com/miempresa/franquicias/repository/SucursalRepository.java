package com.miempresa.franquicias.repository;

import com.miempresa.franquicias.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByFranquiciaId(Long franquiciaId);
}
